package com.astery.xapplication.repository.localDataStorage

import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*

interface LocalStorage {
    suspend fun getEventsForDate(date:Calendar):List<Event>
    suspend fun getTemplate(id:String):EventTemplate
    suspend fun getTemplatesForCategory(category: EventCategory):List<EventTemplate>
    suspend fun getEvent(id:Int):Event
    suspend fun getDescriptionForEvent(event: Event)
    suspend fun addEvent(event:Event)
    suspend fun addTemplate(template:EventTemplate)
    suspend fun addTemplates(templates:List<EventTemplate>)
    suspend fun clearEvents()
    suspend fun clearEventTemplates()

    suspend fun close()

}