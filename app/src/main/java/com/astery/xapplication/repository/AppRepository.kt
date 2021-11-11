package com.astery.xapplication.repository

import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.localDataStorage.LocalStorage
import com.astery.xapplication.repository.preferences.PreferenceEntity
import com.astery.xapplication.repository.preferences.Preferences
import com.astery.xapplication.repository.remoteDataStorage.RemoteStorage
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(@set:Inject var remoteStorage: RemoteStorage,
                                        @set:Inject var localStorage: LocalStorage,
                                        @set:Inject var prefs: Preferences):Repository {
    override suspend fun getEventsByDay(date: Calendar): List<Event> {
        return localStorage.getEventsForDate(Repository.clearDate(date))
    }

    override suspend fun getDescriptionForEvent(event: Event) {
        localStorage.getDescriptionForEvent(event)
    }

    override suspend fun updateEventTemplates() {
        val events = remoteStorage.getEventTemplates(Date(prefs.getLong(RepPrefs.DayOfLastUpdate)))
        if (events.isNotEmpty()) {
            localStorage.addTemplates(events)
            prefs.set(RepPrefs.DayOfLastUpdate, Date().time)
        }
    }

    override suspend fun getEventTemplatesForCategory(category: EventCategory):List<EventTemplate> {
        return localStorage.getTemplatesForCategory(category)
    }

    override suspend fun addEvent(event: Event) {
        localStorage.addEvent(event)
    }

    private enum class RepPrefs: PreferenceEntity {
        /**
         * day of last update values from remote. Need to get only new info
         * */
        DayOfLastUpdate{
            override val default: Long = 0
        }
    }
}