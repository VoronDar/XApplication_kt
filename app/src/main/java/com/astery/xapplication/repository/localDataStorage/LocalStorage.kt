package com.astery.xapplication.repository.localDataStorage

import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*

interface LocalStorage {
    suspend fun getEventsForDate(date:Calendar):List<Event>
    /** get just template, don't load questions */
    suspend fun getTemplate(id:Int):EventTemplate
    suspend fun getTemplatesForCategory(category: EventCategory):List<EventTemplate>
    suspend fun getEvent(id:Int):Event
    /** get template, selected answer, questions */
    suspend fun getDescriptionForEvent(event: Event):EventTemplate
    suspend fun addEvent(event:Event)
    suspend fun addTemplate(template:EventTemplate)
    suspend fun addTemplates(templates:List<EventTemplate>)
    suspend fun addArticle(article: Article)
    suspend fun getArticle(id:Int):Article
    suspend fun getArticlesWithTag(tags:List<Int>):List<Article>
    /** searching for articles using keyword () */
    suspend fun getArticlesWithTagAndKeyWord(tags:List<Int>, key:String):List<Article>
    suspend fun getItemsForArticle(articleId:Int):List<Item>
    suspend fun getAdvicesForItem(itemId: Int): List<Advice>
    suspend fun getItemBody(itemId:Int):Item

    /** get answers and questions for event */
    suspend fun getQuestionsAndSelectedAnswersForEvent(eventId:Int):List<Question>
    suspend fun addAnswer(answer: Answer)
    suspend fun addAnswers(answers: List<Answer>)
    suspend fun addQuestion(question: Question)
    suspend fun addQuestions(question: List<Question>)

    suspend fun updateSelectedAnswer(eventId:Int, question:Question)

    suspend fun deleteEvents()
    suspend fun deleteEventTemplates()
    suspend fun deleteArticles()
    suspend fun deleteQuestions()
    suspend fun deleteAnswers()

    suspend fun close()
    suspend fun getQuestionsWithAnswers(templateId: Int):List<Question>
    suspend fun deleteEvent(event: Event)
    suspend fun changeFeetBackStateForAdvice(id: Int, feedBackState: FeedBackState)
}