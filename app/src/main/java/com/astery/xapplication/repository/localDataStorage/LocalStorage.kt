package com.astery.xapplication.repository.localDataStorage

import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import java.util.*

interface LocalStorage {
    suspend fun getEventsForDate(date:Date):List<Event>
    suspend fun getTemplate(id:String):EventTemplate
    suspend fun getEvent(id:String):Event
    suspend fun getDescriptionForEvent(event: Event)
    suspend fun addEvent(event:Event)
    suspend fun addTemplate(template:EventTemplate)
    suspend fun clearEvents()
    suspend fun clearEventTemplates()

    suspend fun close()

}