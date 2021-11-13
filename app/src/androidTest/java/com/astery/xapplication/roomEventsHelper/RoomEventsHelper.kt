package com.astery.xapplication.roomEventsHelper
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*

object RoomEventsHelper {
    fun getEmptyEvent(id:Int, date: Date = Date()):Event{
        return Event(id, 0, null, date)
    }
    fun getEmptyTemplate(id:Int): EventTemplate {
        return EventTemplate(id, "id", "description", EventCategory.Medicine)
    }

}