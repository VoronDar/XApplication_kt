package com.astery.xapplication.repository.localDataStorage

import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*

interface LocalStorage {
    suspend fun getEventsForDate(date:Calendar):List<Event>
    suspend fun getTemplate(id:Int):EventTemplate
    suspend fun getTemplatesForCategory(category: EventCategory):List<EventTemplate>
    suspend fun getEvent(id:Int):Event
    suspend fun getDescriptionForEvent(event: Event)
    suspend fun addEvent(event:Event)
    suspend fun addTemplate(template:EventTemplate)
    suspend fun addTemplates(templates:List<EventTemplate>)
    suspend fun deleteEvents()
    suspend fun deleteEventTemplates()
    suspend fun deleteArticles()
    suspend fun addArticle(article: Article)
    suspend fun getArticle(id:Int):Article
    suspend fun getArticlesWithTag(tags:List<Int>):List<Article>
    /** searching for articles using keyword () */
    suspend fun getArticlesWithTagAndKeyWord(tags:List<Int>, key:String):List<Article>
    suspend fun getItemsForArticle(articleId:Int):List<Item>
    suspend fun getAdvicesForItem(itemId: Int): List<Advice>

    suspend fun close()

}