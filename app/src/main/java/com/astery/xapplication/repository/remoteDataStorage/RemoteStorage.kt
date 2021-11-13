package com.astery.xapplication.repository.remoteDataStorage

import com.astery.xapplication.model.entities.EventTemplate
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteStorage @Inject constructor(){
    suspend fun getEventTemplates(lastUpdate:Date): List<EventTemplate> {
        return TODO("some fb code")
    }

}