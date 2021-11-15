package com.astery.xapplication.repository.localDataStorage

import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * actual implementation of localStorage
 * Uses Room
 * */
@Singleton
class AppLocalStorage @Inject constructor(@set:Inject var appDatabase: AppDatabase) :LocalStorage{
    override suspend fun getEventsForDate(date: Calendar): List<Event> {
        return appDatabase.eventDao().getEventsByTime(date.time)
    }

    override suspend fun getDescriptionForEvent(event:Event):EventTemplate {
        val template = appDatabase.eventDao().getEventTemplate(event.templateId)
        template.questions = getQuestionsAndSelectedAnswersForEvent(event.id!!)
        return template
    }

    override suspend fun deleteEvents() {
        appDatabase.eventDao().deleteEvents()
        appDatabase.eventDao().deleteAnswerAndEventRelations()
    }

    override suspend fun deleteEventTemplates() {
        appDatabase.eventDao().deleteEventTemplates()
    }

    override suspend fun close() {
        appDatabase.close()
    }

    override suspend fun addEvent(event: Event) {
        val eventId = appDatabase.eventDao().addEvent(event)
        for (i in event.template!!.questions!!){
            if (i.selectedAnswer != null){
                appDatabase.eventDao().addAnswerAndEvent(AnswerAndEvent(eventId.toInt(), i.selectedAnswer!!.id))
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

    override suspend fun getArticlesWithTag(tags: List<Int>): List<Article> {
        return appDatabase.articleDao().getArticlesWithTag(tags)
    }

    override suspend fun getArticlesWithTagAndKeyWord(tags: List<Int>, key: String): List<Article> {
        return appDatabase.articleDao().getArticlesWIthTagAndKeyWord(tags, key)
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
        appDatabase.eventDao().addQuestions(question)
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

}