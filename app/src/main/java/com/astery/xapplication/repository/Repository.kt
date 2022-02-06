package com.astery.xapplication.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.paging.PagingSource
import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.localDataStorage.LocalStorage
import com.astery.xapplication.repository.preferences.Preferences
import com.astery.xapplication.repository.remoteDataStorage.*
import com.astery.xapplication.ui.loadingState.InternetConnectionException
import com.astery.xapplication.ui.loadingState.UnexpectedBugException
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KSuspendFunction1
import kotlin.reflect.KSuspendFunction2


@Singleton
class Repository @Inject constructor(
    @set:Inject var remoteStorage: RemoteStorage,
    @set:Inject var localStorage: LocalStorage,
    @set:Inject var prefs: Preferences,
    @set:Inject var askForUpdateMgr: AskForUpdateManager,
    @ApplicationContext val context: Context
) {

    suspend fun getEventsByDay(date: Calendar): List<Event> {
        return localStorage.getEventsForDate(clearDate(date))
    }

    /** load eventTemplate, questions, answers */
    suspend fun getDescriptionForEvent(event: Event): EventTemplate {
        return localStorage.getDescriptionForEvent(event)
    }

    suspend fun getEventTemplatesForCategory(category: EventCategory): Result<List<EventTemplate>> {
        return getValues(
            localStorage::getTemplatesForCategory,
            remoteStorage::getTemplatesForCategory,
            localStorage::addTemplates,
            category,
            EvTemplateREU(category),
            isCanBeNothing = false
        )
    }

    suspend fun getImageForEventTemplate(eventTemplate: EventTemplate): Bitmap? {
        return getImage(eventTemplate.id, StorageSource.Templates)
    }

    suspend fun getImageForArticle(id: Int): Bitmap? {
        return getImage(id, StorageSource.Articles)
    }

    suspend fun getImageForItem(id: Int): Bitmap? {
        return getImage(id, StorageSource.Items)
    }

    private suspend fun getImage(id: Int, source: StorageSource): Bitmap? {
        var bitmap = localStorage.getImage(id.toString(), source)
        if (bitmap == null && isOnline()) {
            bitmap = remoteStorage.getImg(source, id.toString())
            if (bitmap != null) localStorage.addImage(bitmap, id.toString(), source)
        }
        return bitmap
    }


    suspend fun addEvent(event: Event) {
        localStorage.addEvent(event)
    }

    suspend fun getItemsForArticle(articleId: Int): Result<List<Item>> {
        return getValues(
            localStorage::getItemsForArticle,
            remoteStorage::getItemsForArticle,
            localStorage::addItems,
            articleId,
            null,
            isCanBeNothing = false
        )
    }

    /** get data from remote, get feedback from local, combine, save*/
    suspend fun getAdvicesForItem(itemId: Int): Result<List<Advice>> {
        Timber.d("ask for advices - $itemId")

        val result = remoteStorage.getAdvicesForItem(itemId, -1)
        return if (result.isSuccess) {
            val list = convertFromRemote(result.getOrThrow())
            val fromLocal = localStorage.getAdvicesForItem(itemId)
            for (i in list) {
                if (fromLocal.contains(i)) {
                    val ad = fromLocal[fromLocal.indexOf(i)]
                    i.feedback = ad.feedback
                }
            }
            localStorage.addAdvices(list)
            Result.success(list)
        } else Result.failure(result.exceptionOrNull()!!)
    }

    /** we have itemId, and advices. We need body, name and image. Get item from db, combine*/
    suspend fun setItemBody(item: Item): Result<Item> {
        val gotItem = getValues(
            localStorage::getItemBody,
            remoteStorage::getItemById,
            localStorage::addItems,
            item.id,
            null,
            isCanBeNothing = false
        )
        if (gotItem.isSuccess) item.image = this.getImageForItem(item.id)


        if (gotItem.isFailure) return Result.failure(gotItem.exceptionOrNull()!!)
        return Result.success(
            item.clone(
                name = gotItem.getOrThrow()[0].name,
                body = gotItem.getOrThrow()[0].body
            )
        )
    }

    /*
     * TODO(do something with answers to questions that may not be exist for now and for questions that were added recently, and the user didn't answered them)
     * TODO(do something with questions that came without answers)
     * */
    suspend fun getQuestionsWithAnswers(templateId: Int): Result<List<Question>> {
        return getValues(
            localStorage::getQuestionsWithAnswers, remoteStorage::getQuestionsForTemplate,
            localStorage::addQuestions, templateId, QuestionREU(templateId),
            isCanBeNothing = true
        )
    }

    suspend fun deleteEvent(event: Event) {
        localStorage.deleteEvent(event)
    }

    fun getArticles(sequence: String, tags: List<ArticleTag>): PagingSource<Int, Article> {
        //TODO (make for remote also)
        if (sequence.isEmpty() && tags.isEmpty()) return localStorage.getArticles()
        if (sequence.isNotEmpty() && tags.isNotEmpty()) return localStorage.getArticlesWithTagAndKeyWord(
            tags,
            sequence
        )
        return if (sequence.isNotEmpty()) localStorage.getArticlesWithKeyWord(sequence)
        else localStorage.getArticlesWithTag(tags)
    }

    suspend fun reset() {
        localStorage.reset()
    }


    suspend fun likeAdvice(id: Int, nowLikes: Int, nowDislikes: Int): Boolean {
        return rateAdvice(
            id,
            FeedbackResult(FeedbackField.Like, FeedbackAction.Do, nowLikes, nowDislikes)
        )
    }

    suspend fun dislikeAdvice(id: Int, nowLikes: Int, nowDislikes: Int): Boolean {
        return rateAdvice(
            id,
            FeedbackResult(FeedbackField.Dislike, FeedbackAction.Do, nowLikes, nowDislikes)
        )
    }

    suspend fun cancelLikeAdvice(id: Int, nowLikes: Int, nowDislikes: Int): Boolean {
        return rateAdvice(
            id,
            FeedbackResult(FeedbackField.Like, FeedbackAction.Cancel, nowLikes, nowDislikes)
        )
    }

    suspend fun cancelDislikeAdvice(id: Int, nowLikes: Int, nowDislikes: Int): Boolean {
        return rateAdvice(
            id,
            FeedbackResult(FeedbackField.Dislike, FeedbackAction.Cancel, nowLikes, nowDislikes)
        )
    }

    /** like/dislike advice (or remove them). Update the field in remote, if it was successful - update in local*/
    private suspend fun rateAdvice(id: Int, result: FeedbackResult): Boolean {
        if (!isOnline()) return false
        val isComplete = remoteStorage.updateAdviceField(id, result)
        Timber.d("tried to rate advice $id. isSuccess - $isComplete")
        if (isComplete) localStorage.updateAdviceField(id, result)
        return isComplete
    }

    private suspend fun rateArticle(id: Int, result: FeedbackResult): Boolean {
        if (!isOnline()) return false
        val isComplete = remoteStorage.updateArticleField(id, result)
        Timber.d("tried to rate article $id. isSuccess - $isComplete")
        if (isComplete) localStorage.updateArticleField(id, result)
        return isComplete
    }

    suspend fun likeArticle(id: Int, nowLikes: Int, nowDislikes: Int): Boolean {
        return rateArticle(
            id,
            FeedbackResult(FeedbackField.Like, FeedbackAction.Do, nowLikes, nowDislikes)
        )
    }

    suspend fun dislikeArticle(id: Int, nowLikes: Int, nowDislikes: Int): Boolean {
        return rateArticle(
            id,
            FeedbackResult(FeedbackField.Dislike, FeedbackAction.Do, nowLikes, nowDislikes)
        )
    }

    suspend fun cancelLikeArticle(id: Int, nowLikes: Int, nowDislikes: Int): Boolean {
        return rateArticle(
            id,
            FeedbackResult(FeedbackField.Like, FeedbackAction.Cancel, nowLikes, nowDislikes)
        )
    }

    suspend fun cancelDislikeArticle(id: Int, nowLikes: Int, nowDislikes: Int): Boolean {
        return rateArticle(
            id,
            FeedbackResult(FeedbackField.Dislike, FeedbackAction.Cancel, nowLikes, nowDislikes)
        )
    }


    private fun isOnline(): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connMgr.activeNetwork ?: return false
            val actNw = connMgr.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connMgr.activeNetworkInfo?.isConnected ?: false
        }

    }

    /**
     * get values from remote if it is required to check for updates, save them to local storage
     *
     * @param localGet - return list from local storage. localSet also work this way
     * @param remoteGet - return result<list> from remote storage. has a parameter lastUpdated (Int) but actually it may not be used in actual function
     * @param matcher - parameter for getting data. For example. GetEventTemplateByCategory. Matcher is a category
     * @param remUpdate - settings for the entity, that has special update settings. (for example, if it is required to check for updates every X days)
     * if remUpdate == null, this value must always be loaded from remote.
     * @param isCanBeNothing - when list in local is empty and this variable = true, means that it is ok, return success. Otherwise - return failure
     *
     * T - matcher type; R - result type
     *
     * @return list with values. throwable in Result.failure must be extended from LoadingErrorReason
     *
     */

    // TODO(сделать так,чтобы у тех, кто пришел с ошибкой сбрасывался remUpdate)
    private suspend fun <T, R> getValues(
        localGet: KSuspendFunction1<T, List<R>>,
        remoteGet: KSuspendFunction2<T, Int, Result<List<RemoteEntity<R>>>>,
        localSet: KSuspendFunction1<List<R>, Unit>,
        matcher: T,
        remUpdate: RemEntityUpdate?,
        isCanBeNothing: Boolean
    ): Result<List<R>> {

        // check if it is need (and it is possible) to get data from remote without looking in local
        if (isOnline() && (remUpdate == null || askForUpdateMgr.isNeedToUpdate(remUpdate))) {

            val l = getFromRemoteAndSave(
                matcher,
                remoteGet,
                localSet,
                remUpdate,
                askForUpdateMgr.getLastUpdated(remUpdate),
                isCanBeNothing
            )
            // if (the latest data are always got from remote initialy it's not required to look in local)
            if (remUpdate == null) return l
        }
        val list: List<R> = localGet(matcher)

        // if there is no data in local, go and check in remote
        return if (list.isEmpty() && !isCanBeNothing && isOnline()) {
            getFromRemoteAndSave(
                matcher,
                remoteGet,
                localSet,
                remUpdate,
                AskForUpdateManager.FIRST_UPDATE.toInt(),
                isCanBeNothing
            )
        } else if (list.isNotEmpty()) Result.success(list)
        else if (isCanBeNothing) Result.success(listOf())
        else if (!isOnline()) Result.failure(InternetConnectionException())
        else Result.failure(UnexpectedBugException())
    }


    private suspend fun <T, R> getFromRemoteAndSave(
        matcher: T,
        remoteGet: KSuspendFunction2<T, Int, Result<List<RemoteEntity<R>>>>,
        localSet: KSuspendFunction1<List<R>, Unit>,
        remUpdate: RemEntityUpdate?,
        lastUpdated: Int,
        isCanBeNothing: Boolean
    ): Result<List<R>> {
        val remoteResult = remoteGet(matcher, lastUpdated)
        // save to local if the result is successful
        var result: Result<List<R>>
        if (remoteResult.isSuccess) {

            val remoteList = remoteResult.getOrThrow()
            result = Result.success(convertFromRemote(remoteList))
            localSet(result.getOrThrow())

            if (result.getOrThrow().isEmpty() && !isCanBeNothing)
                result = Result.failure(InternetConnectionException())


            // change values that required for checking for updates (if it required)
            if (remoteList.isNotEmpty() && remUpdate != null) {
                askForUpdateMgr.setUpdated(
                    remUpdate,
                    remoteList.maxByOrNull { it.lastUpdated }!!.lastUpdated
                )
            }

        } else
            result = Result.failure(remoteResult.exceptionOrNull()!!)
        return result
    }

    private fun <T> convertFromRemote(list: List<RemoteEntity<T>>): List<T> {
        val l = ArrayList<T>()
        for (i in list) {
            l.add(i.convertFromRemote())
        }
        return l
    }


    companion object {
        /** prepare calendar to be used for db (left just date)
         * you must not use calendar object without this clearing
         * */
        fun clearDate(cal: Calendar): Calendar {
            cal[Calendar.HOUR_OF_DAY] = cal.getActualMinimum(Calendar.HOUR_OF_DAY)
            cal[Calendar.MINUTE] = cal.getActualMinimum(Calendar.MINUTE)
            cal[Calendar.SECOND] = cal.getActualMinimum(Calendar.SECOND)
            cal[Calendar.MILLISECOND] = cal.getActualMinimum(Calendar.MILLISECOND)
            return cal
        }
    }

}

interface RemoteEntity<T> {
    var id: Int
    var lastUpdated: Int
    fun convertFromRemote(): T
}

data class FeedbackResult(
    val field: FeedbackField,
    val action: FeedbackAction,
    val nowLikes: Int = 0,
    val nowDislikes: Int = 0
)

enum class FeedbackField {
    Like, Dislike
}

enum class FeedbackAction {
    Do, Cancel
}