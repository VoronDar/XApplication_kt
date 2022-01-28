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

    suspend fun getEventTemplatesForCategory(category: EventCategory): List<EventTemplate> {
        // TODO(UI show that there is no event templates)
        return getValues(
            localStorage::getTemplatesForCategory,
            remoteStorage::getTemplatesForCategory,
            localStorage::addTemplates,
            category,
            EvTemplateREU(category)
        )
    }

    suspend fun getImageForEventTemplate(eventTemplate: EventTemplate): Bitmap? {
        var bitmap = localStorage.getImage(eventTemplate.id.toString())
        if (bitmap == null && isOnline()) {
            bitmap = remoteStorage.getImg(eventTemplate.id.toString())
            if (bitmap != null) localStorage.addImage(bitmap, eventTemplate.name)
        }
        return bitmap
    }

    suspend fun addEvent(event: Event) {
        localStorage.addEvent(event)
    }

    suspend fun getArticle(articleId: Int): Article {
        return localStorage.getArticle(articleId)
    }

    suspend fun getItemsForArticle(articleId: Int): List<Item> {
        return localStorage.getItemsForArticle(articleId)
    }

    // TODO(show in UI that there is no advices loaded)
    suspend fun getAdvicesForItem(itemId: Int): List<Advice> {
        Timber.d("ask for advices - $itemId")
        return getValues(
            localStorage::getAdvicesForItem,
            remoteStorage::getAdvicesForItem,
            localStorage::addAdvices,
            itemId,
            null
        )
    }

    suspend fun getEventDescription(eventId: Int): List<Question> {
        return localStorage.getQuestionsAndSelectedAnswersForEvent(eventId)
    }

    /** we have itemId, and advices. We need body and name. Get item from db, combine*/
    suspend fun setItemBody(item: Item): Item {
        // TODO(может вернуться null)
        //val gotItem = localStorage.getItemBody(item.id!!)
        val gotItem =  getValues(localStorage::getItemBody, remoteStorage::getItemById, localStorage::addItems, item.id!!, null)
        if (gotItem.isEmpty()) return item
        return item.clone(name = gotItem[0].name, body = gotItem[0].body)
    }

    /*
     * TODO(do something with answers to questions that may not be exist for now and for questions that were added recently, and the user didn't answered them)
     * TODO(do something with questions that came without answers)
     * */
    suspend fun getQuestionsWithAnswers(templateId: Int): List<Question> {
        return getValues(
            localStorage::getQuestionsWithAnswers, remoteStorage::getQuestionsForTemplate,
            localStorage::addQuestions, templateId, QuestionREU(templateId)
        )
    }

    suspend fun deleteEvent(event: Event) {
        localStorage.deleteEvent(event)
    }

    fun getArticles(): PagingSource<Int, Article> {
        return localStorage.getArticlesWithTag(listOf(GenderTag.Man.ordinal))
    }

    suspend fun reset() {
        localStorage.reset()
    }


    suspend fun likeAdvice(id: Int) {}
    suspend fun dislikeAdvice(id: Int) {}
    suspend fun cancelLikeAdvice(id: Int) {}
    suspend fun cancelDislikeAdvice(id: Int) {}
    suspend fun changeFeetBackStateForAdvice(id: Int, feedBackState: FeedBackState) {
        localStorage.changeFeetBackStateForAdvice(id, feedBackState)
    }

    suspend fun likeArticle(id: Int) {}
    suspend fun dislikeArticle(id: Int) {}
    suspend fun cancelLikeArticle(id: Int) {}
    suspend fun cancelDislikeArticle(id: Int) {}
    suspend fun changeFeetBackStateForArticle(id: Int, feedBackState: FeedBackState) {
        localStorage.changeFeetBackStateForArticle(id, feedBackState)
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

    // TODO(lastUpdated выражает версию обновления, но в локалке сохраняется последняя версия.
    //  Получается будто бы обновилось все. но обновилось только одно. Предполагаемое решеие - сохранять в преференсах уточнение для каждого случая отдельно)
    // T - parameter. R - result
    // check if it is time to check for updates. Get data from remote if it is true. Load to local
    // load dara from remote if there is no data in local. Load from local if everything fine
    // get empty list if something goes wrong
    // (in normal case, if there is no internet connection, and the user hasn't loaded data earlier)
    private suspend fun <T, R> getValues(
        localGet: KSuspendFunction1<T, List<R>>,
        remoteGet: KSuspendFunction2<T, Int, List<RemoteEntity<R>>>,
        localSet: KSuspendFunction1<List<R>, Unit>,
        matcher: T,
        remUpdate: RemEntityUpdate?
    ): List<R> {
        if (isOnline() && (remUpdate == null || askForUpdateMgr.isNeedToUpdate(remUpdate))) {
            if (remUpdate != null)
                Timber.d("need to check updates for ${remUpdate::class.simpleName}")
            val l = getFromRemoteAndSave(
                matcher,
                remoteGet,
                localSet,
                remUpdate,
                askForUpdateMgr.getLastUpdated(remUpdate)
            )
            if (remUpdate == null) return l
        }
        val list: List<R> = localGet(matcher)
        return if (list.isEmpty() && isOnline()) {
            getFromRemoteAndSave(
                matcher,
                remoteGet,
                localSet,
                remUpdate,
                AskForUpdateManager.FIRST_UPDATE.toInt()
            )
        } else {
            if (remUpdate != null)
                Timber.d("Got ${remUpdate::class.simpleName} from local. Amount of elements - ${list.size}")
            list
        }
    }

    private suspend fun <T, R> getFromRemoteAndSave(
        matcher: T,
        remoteGet: KSuspendFunction2<T, Int, List<RemoteEntity<R>>>,
        localSet: KSuspendFunction1<List<R>, Unit>,
        remUpdate: RemEntityUpdate?,
        lastUpdated: Int
    ): List<R> {
        val remoteList = remoteGet(matcher, lastUpdated)
        val list = convertFromRemote(remoteList)
        localSet(list)
        if (remoteList.isNotEmpty() && remUpdate != null) {
            askForUpdateMgr.setUpdated(
                remUpdate,
                remoteList.maxByOrNull { it.lastUpdated!! }!!.lastUpdated!!
            )
        }
        return list
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
    var id:Int
    var lastUpdated:Int
    fun convertFromRemote(): T
}