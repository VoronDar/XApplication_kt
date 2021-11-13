package com.astery.xapplication.repository

import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*

interface Repository {
    suspend fun getEventsByDay(date: Calendar):List<Event>
    /** load eventTemplate, questions, answers */
    suspend fun getDescriptionForEvent(event: Event)
    /** check for updates and load new event templates from remote */
    suspend fun updateEventTemplates()
    suspend fun getEventTemplatesForCategory(category: EventCategory):List<EventTemplate>
    suspend fun addEvent(event: Event)
    suspend fun getArticle(articleId:Int): Article
    suspend fun getItemsForArticle(articleId: Int):List<Item>
    suspend fun getAdvicesForItem(itemId: Int):List<Advice>


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