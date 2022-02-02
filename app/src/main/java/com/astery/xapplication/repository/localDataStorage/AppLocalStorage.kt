package com.astery.xapplication.repository.localDataStorage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.paging.PagingSource
import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.FeedbackAction
import com.astery.xapplication.repository.FeedbackField
import com.astery.xapplication.repository.FeedbackResult
import com.astery.xapplication.repository.remoteDataStorage.StorageSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * actual implementation of localStorage
 * Uses Room
 * */
@Singleton
class AppLocalStorage @Inject constructor(
    @set:Inject var appDatabase: AppDatabase,
    @ApplicationContext var context: Context
) : LocalStorage {
    override suspend fun getEventsForDate(date: Calendar): List<Event> {
        return appDatabase.eventDao().getEventsByTime(date.time)
    }

    override suspend fun getDescriptionForEvent(event: Event): EventTemplate {
        val template = appDatabase.eventDao().getEventTemplate(event.templateId)
        template.questions = getQuestionsAndSelectedAnswersForEvent(event.id!!)
        return template
    }

    override suspend fun deleteEvents() {
        appDatabase.eventDao().deleteEvents()
        appDatabase.eventDao().deleteAnswerAndEventRelations()
    }

    override suspend fun deleteEvent(event: Event) {
        appDatabase.eventDao().deleteEvent(event.id!!)
        appDatabase.eventDao().deleteAnswerAndEventRelation(event.id!!)
    }


    override suspend fun deleteEventTemplates() {
        appDatabase.eventDao().deleteEventTemplates()
    }

    override suspend fun close() {
        appDatabase.close()
    }

    override suspend fun addEvent(event: Event) {
        val eventId = appDatabase.eventDao().addEvent(event)
        if (event.template!!.questions != null)
            for (i in event.template!!.questions!!) {
                if (i.selectedAnswer != null) {
                    appDatabase.eventDao()
                        .addAnswerAndEvent(AnswerAndEvent(eventId.toInt(), i.selectedAnswer!!.id))
                }
            }
    }

    override suspend fun addTemplate(template: EventTemplate) {
        appDatabase.eventDao().addEventTemplate(template)
    }

    override suspend fun addTemplates(templates: List<EventTemplate>) {
        appDatabase.eventDao().addEventTemplates(templates)
    }

    override suspend fun getTemplate(id: Int): EventTemplate {
        return appDatabase.eventDao().getEventTemplate(id)
    }

    override suspend fun getEvent(id: Int): Event {
        return appDatabase.eventDao().getEvent(id)
    }

    override suspend fun getTemplatesForCategory(category: EventCategory): List<EventTemplate> {
        return appDatabase.eventDao().getEventTemplatesWithCategory(category)
    }

    override suspend fun addArticle(article: Article) {
        appDatabase.articleDao().addArticleWithTags(article)
    }

    override suspend fun getArticle(id: Int): Article {
        return appDatabase.articleDao().getArticleById(id)
    }

    override fun getArticlesWithTag(tags: List<ArticleTag>): PagingSource<Int, Article> {
        return appDatabase.articleDao().getArticlesWithTag(convertListOfTagsToListOfId(tags))
    }

    override fun getArticlesWithTagAndKeyWord(
        tags: List<ArticleTag>,
        key: String
    ): PagingSource<Int, Article> {
        return appDatabase.articleDao()
            .getArticlesWIthTagAndKeyWord(convertListOfTagsToListOfId(tags), key)
    }

    override fun getArticlesWithKeyWord(sequence: String): PagingSource<Int, Article> {
        return appDatabase.articleDao().getArticlesWithKeyWord(sequence)
    }

    override fun getArticles(): PagingSource<Int, Article> {
        return appDatabase.articleDao().getArticles()
    }

    private fun convertListOfTagsToListOfId(tags: List<ArticleTag>): List<Int> {
        val list = arrayListOf<Int>()
        for (i in tags) {
            list.add(i.id)
        }
        return list
    }

    override suspend fun deleteArticles() {
        appDatabase.articleDao().deleteArticlesWithTags()
    }

    override suspend fun getItemsForArticle(articleId: Int): List<Item> {
        return appDatabase.articleDao().getItemsByParentId(articleId)
    }

    override suspend fun getAdvicesForItem(itemId: Int): List<Advice> {
        return appDatabase.articleDao().getAdvisesForItem(itemId)
    }

    override suspend fun getQuestionsAndSelectedAnswersForEvent(eventId: Int): List<Question> {
        return appDatabase.eventDao().getAnswersAndQuestionsForEvent(eventId)
    }

    override suspend fun updateSelectedAnswer(eventId: Int, question: Question) {
        appDatabase.eventDao().updateAnswerAndEvent(eventId, question)
    }

    override suspend fun addAnswer(answer: Answer) {
        appDatabase.eventDao().addAnswer(answer)
    }

    override suspend fun addAnswers(answers: List<Answer>) {
        appDatabase.eventDao().addAnswers(answers)
    }

    override suspend fun addQuestions(question: List<Question>) {
        Timber.d("add questions $question")
        appDatabase.eventDao().addQuestionsWithAnswers(question)
    }

    override suspend fun addQuestion(question: Question) {
        appDatabase.eventDao().addQuestion(question)
    }

    override suspend fun deleteQuestions() {
        appDatabase.eventDao().deleteQuestions()
    }

    override suspend fun deleteAnswers() {
        appDatabase.eventDao().deleteAnswers()
    }

    override suspend fun addItems(items: List<Item>) {
        appDatabase.articleDao().addItems(items)
    }

    override suspend fun getItemBody(itemId: Int): List<Item> {
        val item = appDatabase.articleDao().getItemBody(itemId) ?: return listOf()
        return listOf(item)
    }


    override suspend fun getQuestionsWithAnswers(templateId: Int): List<Question> {
        return appDatabase.eventDao().getAnswersAndQuestionsForTemplate(templateId)
    }

    override suspend fun changeFeetBackStateForAdvice(id: Int, feedBackState: FeedBackState) {
        appDatabase.articleDao().updateAdviceFeedbackState(id, feedBackState)
    }

    override suspend fun changeFeetBackStateForArticle(id: Int, feedBackState: FeedBackState) {
        appDatabase.articleDao().updateArticleFeedbackState(id, feedBackState)
    }


    override suspend fun addArticleWithTag(article: Article) {
        appDatabase.articleDao().addArticleWithTags(article)
    }

    override suspend fun addAdvices(advices: List<Advice>) {
        appDatabase.articleDao().addAdvises(advices)
    }

    override suspend fun addImage(bitmap: Bitmap, name: String, source: StorageSource) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val f = File(context.cacheDir, getImageName(name, source))
                try {
                    f.createNewFile()
                    val bos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                    val bitmapdata = bos.toByteArray()
                    val fos = FileOutputStream(f)
                    fos.write(bitmapdata)
                    fos.flush()
                    fos.close()
                } catch (e: IOException) {
                    Timber.d("failed to create file ${name}.jpeg ${e.localizedMessage}")
                }
            }
        }

    }

    override suspend fun getImage(name: String, source: StorageSource): Bitmap? {
        val f = File(context.cacheDir, getImageName(name, source))
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bitmap = BitmapFactory.decodeStream(fis)
        fis?.close()
        Timber.d("trying to get bitmap from local - $name.jpeg - $bitmap")
        return bitmap
    }

    private fun getImageName(name: String, storage: StorageSource): String {
        return "${storage.getFolderName()}_$name.jpeg"
    }

    override suspend fun updateAdviceField(id: Int, result: FeedbackResult) {

        when (result.field) {
            FeedbackField.Like -> appDatabase.articleDao().likeAdvice(
                id,
                if (result.action == FeedbackAction.Do) result.nowLikes + 1 else result.nowLikes - 1,
                if (result.action == FeedbackAction.Do) FeedBackState.Like else FeedBackState.None
            )
            FeedbackField.Dislike -> appDatabase.articleDao().dislikeAdvice(
                id,
                if (result.action == FeedbackAction.Do) result.nowDislikes + 1 else result.nowDislikes - 1,
                if (result.action == FeedbackAction.Do) FeedBackState.Dislike else FeedBackState.None
            )
        }
    }

    override suspend fun updateArticleField(
        id: Int,
        result: FeedbackResult
    ) {
        when (result.field) {
            FeedbackField.Like -> appDatabase.articleDao().likeArticle(
                id,
                if (result.action == FeedbackAction.Do) result.nowLikes + 1 else result.nowLikes - 1,
                if (result.action == FeedbackAction.Do) FeedBackState.Like else FeedBackState.None
            )
            FeedbackField.Dislike -> appDatabase.articleDao().dislikeArticle(
                id,
                if (result.action == FeedbackAction.Do) result.nowDislikes + 1 else result.nowDislikes - 1,
                if (result.action == FeedbackAction.Do) FeedBackState.Dislike else FeedBackState.None
            )
        }

    }

    override suspend fun reset() {
        appDatabase.eventDao().deleteEventTemplates()
        appDatabase.eventDao().deleteEvents()
        appDatabase.eventDao().deleteAnswers()
        appDatabase.eventDao().deleteQuestions()
        appDatabase.articleDao().deleteArticles()
    }
}