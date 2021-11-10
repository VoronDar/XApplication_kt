package com.astery.xapplication.repository

import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.repository.localDataStorage.LocalStorage
import com.astery.xapplication.repository.remoteDataStorage.RemoteStorage
import java.util.*
import javax.inject.Inject

class AppRepository @Inject constructor(@set:Inject var remoteStorage: RemoteStorage,
                                        @set:Inject var localStorage: LocalStorage):Repository {
    override suspend fun getEventsByDay(date: Calendar): List<Event> {
        return localStorage.getEventsForDate(Repository.clearDate(date).time)
    }

    override suspend fun getInfoForEvent(event: Event) {
        localStorage.getDescriptionForEvent(event)
    }
}