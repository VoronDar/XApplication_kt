package com.astery.xapplication.model.remote

import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.RemoteEntity


/** this class must not be used anywhere except getting data from firestore
 * (due to firestore features. It requires variables as vars and also it can't work with enums))
 * I added these remote classes because I don't want to mess the original ones with some variables.
 * */
class EventTemplateFromRemote : EventTemplate(0, "", "", EventCategory.Dating),
    RemoteEntity<EventTemplate> {
    var category:Int = EventCategory.Mood.ordinal
    override var lastUpdated:Int = 0
    override fun convertFromRemote(): EventTemplate {
        return EventTemplate(id, name, body, EventCategory.values()[category])
    }
}