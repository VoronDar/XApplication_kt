package com.astery.xapplication.repository.remoteDataStorage

import com.astery.xapplication.model.entities.Event

interface RemoteStorage {
    suspend fun getEventTemplates():List<Event>
}