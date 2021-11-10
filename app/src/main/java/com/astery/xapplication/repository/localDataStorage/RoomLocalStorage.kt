package com.astery.xapplication.repository.localDataStorage

import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import java.util.*
import javax.inject.Inject

class RoomLocalStorage @Inject constructor(@set:Inject var appDatabase: AppDatabase) :LocalStorage{
    override suspend fun getEventsForDate(date: Date): List<Event> {
        return appDatabase.eventDao().getEventsByTime(date)
    }

    override suspend fun getDescriptionForEvent(event: Event) {
        // TODO(questions, advises)
        event.template = appDatabase.eventDao().getEventTemplate(event.id)
    }

    override suspend fun clearEvents() {
        appDatabase.eventDao().deleteEvents()
    }

    override suspend fun clearEventTemplates() {
        appDatabase.eventDao().deleteEventTemplates()
    }

    override suspend fun close() {
        appDatabase.close()
    }

    override suspend fun addEvent(event: Event) {
        appDatabase.eventDao().addEvent(event)
    }

    override suspend fun addTemplate(template: EventTemplate) {
        appDatabase.eventDao().addEventTemplate(template)
    }

    override suspend fun getTemplate(id: String): EventTemplate {
        return appDatabase.eventDao().getEventTemplate(id)
    }

    override suspend fun getEvent(id: String): Event {
        return appDatabase.eventDao().getEvent(id)
    }
}