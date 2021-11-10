package com.astery.xapplication.repository

import com.astery.xapplication.model.entities.Event
import java.util.*

interface Repository {
    suspend fun getEventsByDay(date: Calendar):List<Event>
    suspend fun getInfoForEvent(event: Event)


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