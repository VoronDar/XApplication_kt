package com.astery.xapplication.repository

import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*

interface Repository {
    suspend fun getEventsByDay(date: Calendar):List<Event>
    suspend fun getDescriptionForEvent(event: Event)
    suspend fun updateEventTemplates()
    suspend fun getEventTemplatesForCategory(category: EventCategory):List<EventTemplate>
    suspend fun addEvent(event: Event)


    companion object {
        /** prepare calendar to be used for db */
        fun clearDate(cal: Calendar): Calendar {
            cal[Calendar.HOUR_OF_DAY] = cal.getActualMinimum(Calendar.HOUR_OF_DAY)
            cal[Calendar.MINUTE] = cal.getActualMinimum(Calendar.MINUTE)
            cal[Calendar.SECOND] = cal.getActualMinimum(Calendar.SECOND)
            cal[Calendar.MILLISECOND] = cal.getActualMinimum(Calendar.MILLISECOND)
            return cal
        }
    }
}