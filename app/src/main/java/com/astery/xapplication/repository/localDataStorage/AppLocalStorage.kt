package com.astery.xapplication.repository.localDataStorage

import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLocalStorage @Inject constructor(@set:Inject var appDatabase: AppDatabase) :LocalStorage{
    override suspend fun getEventsForDate(date: Calendar): List<Event> {
        return appDatabase.eventDao().getEventsByTime(date.time)
    }

    override suspend fun getDescriptionForEvent(event: Event) {
        // TODO(questions, advises)
        event.template = appDatabase.eventDao().getEventTemplate(event.templateId)
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

    override suspend fun addTemplates(templates: List<EventTemplate>) {
        appDatabase.eventDao().addEventTemplates(templates)
    }

    override suspend fun getTemplate(id: String): EventTemplate {
        return appDatabase.eventDao().getEventTemplate(id)
    }

    override suspend fun getEvent(id: Int): Event {
        return appDatabase.eventDao().getEvent(id)
    }

    override suspend fun getTemplatesForCategory(category: EventCategory): List<EventTemplate> {
        return appDatabase.eventDao().getEventTemplatesWithCategory(category)
    }
}