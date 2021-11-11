package com.astery.xapplication.repository.localDataStorage

import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeLocalStorage @Inject constructor():LocalStorage {
    override suspend fun getEventsForDate(date: Calendar): List<Event> {
        val list = ArrayList<Event>()
        if (date.get(Calendar.DAY_OF_MONTH) == 2) {
            list.add(Event(1,  "2", null, null))
            list.add(Event(2, "2", null, null))
            list.add(Event(3, "2", null, null))
            list.add(Event(4, "2", null, null))
        } else  if (date.get(Calendar.DAY_OF_MONTH) == 1){
            list.add(Event(1, "1", null, null))
            list.add(Event(2, "2", null, null))
        }

        return list
    }

    override suspend fun getDescriptionForEvent(event: Event) {
        if (event.id == 1){
            event.template = EventTemplate("id", "name", "asdasdasdasd asd asd sad asd asd as", EventCategory.FEELS)
        }else{
            event.template = EventTemplate("id", "You feel bad", "asdas adas dasdasd asdsadasdsadasdas dasd asd asdsadsad as dasd asd asd asd asd asd as as", EventCategory.CYCLE)
        }
    }

    override suspend fun getTemplatesForCategory(category: EventCategory): List<EventTemplate> {
        val list = ArrayList<EventTemplate>()
        if (category == EventCategory.MEDICINE){
            list.add(EventTemplate("1", "first", "ad ad asdas asd asdasd sad asdas das d", EventCategory.MEDICINE))
            list.add(EventTemplate("2", "second", "lfklsdfljdsl fjdsdlsakfklsdjfldfjlkd ljkfa ds af dsdfadsf dsf a", EventCategory.MEDICINE))
        } else{
            list.add(EventTemplate("3", "third", "adsdasd asd asd asd asd as dasd asd as das dsad asd asd ds asdsd fsf sd d", category))
            list.add(EventTemplate("4", "fourth", "jadsdhasdjas dasd asd asd asdas asd asdasdjashdkjashdkasjhkdhasdh askd asd asd asd sd", category))
            list.add(EventTemplate("5", "fifth", "lfklsdfljdsl fjdsdlsakfklsdjfldfjlkd ljkfa ds af dsdfadsf dsf a", category))
        }
        return list
    }

    override suspend fun getTemplate(id: String): EventTemplate {
        TODO("Not yet implemented")
    }

    override suspend fun getEvent(id: Int): Event {
        TODO("Not yet implemented")
    }

    override suspend fun addEvent(event: Event) {
        Timber.d("ok, I've added event")
    }

    override suspend fun addTemplate(template: EventTemplate) {
        TODO("Not yet implemented")
    }

    override suspend fun addTemplates(templates: List<EventTemplate>) {
        TODO("Not yet implemented")
    }

    override suspend fun clearEvents() {
        TODO("Not yet implemented")
    }

    override suspend fun clearEventTemplates() {
        TODO("Not yet implemented")
    }

    override suspend fun close() {
        TODO("Not yet implemented")
    }
}