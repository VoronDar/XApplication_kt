package com.astery.xapplication.repository.remoteDataStorage

import com.astery.xapplication.model.entities.EventTemplate
import java.util.*

interface RemoteStorage {
    suspend fun getEventTemplates(lastUpdate: Date):List<EventTemplate>
}