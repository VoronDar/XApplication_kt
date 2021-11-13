package com.astery.xapplication.repository.remoteDataStorage

import com.astery.xapplication.model.entities.EventTemplate
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * actual implementation of remote storage
 * uses Firebase-Firestore
 * */
@Singleton
class AppRemoteStorage @Inject constructor():RemoteStorage{
    override suspend fun getEventTemplates(lastUpdate:Date): List<EventTemplate> {
        return TODO("some fb code")
    }

}