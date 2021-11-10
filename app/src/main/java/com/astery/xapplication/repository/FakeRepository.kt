package com.astery.xapplication.repository

import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*
import javax.inject.Inject

class FakeRepository @Inject constructor():Repository {
    override suspend fun getEventsByDay(date: Calendar): List<Event> {
        val list = ArrayList<Event>()
        if (date.get(Calendar.DAY_OF_MONTH) == 2) {
            list.add(Event("1", "2", null, null))
            list.add(Event("2", "2", null, null))
            list.add(Event("3", "2", null, null))
            list.add(Event("4", "2", null, null))
        } else  if (date.get(Calendar.DAY_OF_MONTH) == 1){
            list.add(Event("1", "2", null, null))
            list.add(Event("2", "2", null, null))
        }

        return list

    }

    override suspend fun getInfoForEvent(event: Event) {
        event.template = EventTemplate("id", "name", "asdasdasdasd asd asd sad asd asd as", EventCategory.FEELS)
    }
}