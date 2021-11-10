package com.astery.xapplication.roomEventsHelper
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*

object RoomEventsHelper {
    fun getEmptyEvent(id:String, date: Date = Date()):Event{
        return Event(id, "id", null, date)
    }
    fun getEmptyTemplate(id:String): EventTemplate {
        return EventTemplate(id, "id", "description", EventCategory.MEDICINE)
    }

}