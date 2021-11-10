package com.astery.xapplication.repository.remoteDataStorage

import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.repository.preferences.PreferenceEntity
import com.astery.xapplication.repository.preferences.Preferences
import java.util.*
import javax.inject.Inject

class FbRemoteStorage @Inject constructor(@Inject var prefs: Preferences):RemoteStorage{
    override suspend fun getEventTemplates(): List<Event> {
        prefs.getLong(AppRemote.DayOfLastUpdate)

        prefs.set(AppRemote.DayOfLastUpdate, Date().time)

        return TODO("some fb code")
    }

    private enum class AppRemote: PreferenceEntity {
        /**
         * day of last update values from remote. Need to get only new info
         * */
        DayOfLastUpdate{
            override val default: Long = 0
        }
    }
}