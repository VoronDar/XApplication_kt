package com.astery.xapplication.repository.localDataStorage

import android.graphics.Bitmap
import androidx.paging.PagingSource
import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.FeedbackResult
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
    suspend fun addArticleWithTag(article: Article)
    suspend fun addAdvices(advices:List<Advice>)
    suspend fun getArticle(id:Int):Article
    fun getArticlesWithTag(tags:List<ArticleTag>):PagingSource<Int,Article>
    /** searching for articles using keyword () */
    fun getArticlesWithTagAndKeyWord(tags:List<ArticleTag>, key:String):PagingSource<Int, Article>
    fun getArticles():PagingSource<Int,Article>
    suspend fun getItemsForArticle(articleId:Int):List<Item>
    suspend fun getAdvicesForItem(itemId: Int): List<Advice>
    /** it returns list with just one (or zero) item */
    suspend fun getItemBody(itemId:Int):List<Item>

    /** get answers and questions for event */
    suspend fun getQuestionsAndSelectedAnswersForEvent(eventId:Int):List<Question>
    suspend fun addAnswer(answer: Answer)
    suspend fun addAnswers(answers: List<Answer>)
    suspend fun addItems(items: List<Item>)
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
    suspend fun changeFeetBackStateForArticle(id: Int, feedBackState: FeedBackState)
    suspend fun addImage(bitmap: Bitmap, name: String)
    suspend fun getImage(name:String):Bitmap?

    suspend fun updateAdviceField(id: Int, result: FeedbackResult)
    suspend fun updateArticleField(id: Int, result: FeedbackResult)

    suspend fun reset()
    fun getArticlesWithKeyWord(sequence: String): PagingSource<Int, Article>
}