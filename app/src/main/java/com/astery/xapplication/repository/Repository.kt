package com.astery.xapplication.repository

import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.localDataStorage.LocalStorage
import com.astery.xapplication.repository.preferences.PreferenceEntity
import com.astery.xapplication.repository.preferences.Preferences
import com.astery.xapplication.repository.remoteDataStorage.RemoteStorage
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Repository @Inject constructor(@set:Inject var remoteStorage: RemoteStorage,
                                     @set:Inject var localStorage: LocalStorage,
                                     @set:Inject var prefs: Preferences) {
    
    suspend fun getEventsByDay(date: Calendar): List<Event> {
        return localStorage.getEventsForDate(clearDate(date))
    }

    /** load eventTemplate, questions, answers */
    suspend fun getDescriptionForEvent(event: Event):EventTemplate {
        return localStorage.getDescriptionForEvent(event)
    }

    /** check for updates and load new event templates from remote */
    suspend fun updateEventTemplates() {
        val events = remoteStorage.getEventTemplates(Date(prefs.getLong(RepPrefs.DayOfLastUpdate)))
        if (events.isNotEmpty()) {
            localStorage.addTemplates(events)
            prefs.set(RepPrefs.DayOfLastUpdate, Date().time)
        }
    }

    suspend fun getEventTemplatesForCategory(category: EventCategory):List<EventTemplate> {
        return localStorage.getTemplatesForCategory(category)
    }

    suspend fun addEvent(event: Event) {
        localStorage.addEvent(event)
    }

    suspend fun getArticle(articleId: Int): Article {
        return localStorage.getArticle(articleId)
    }

    suspend fun getItemsForArticle(articleId: Int): List<Item> {
        return localStorage.getItemsForArticle(articleId)
    }

    suspend fun getAdvicesForItem(itemId: Int): List<Advice> {
        return localStorage.getAdvicesForItem(itemId)
    }

    suspend fun getEventDescription(eventId:Int):List<Question>{
        return localStorage.getQuestionsAndSelectedAnswersForEvent(eventId)
    }

    /** we have itemId, and advices. We need body and name. Get item from db, combine*/
    suspend fun setItemBody(item: Item):Item{
        val gotItem = localStorage.getItemBody(item.id!!)
        return item.clone(name = gotItem.name, body = gotItem.body)
    }

    suspend fun getQuestionsWithAnswers(templateId: Int):List<Question> {
        return localStorage.getQuestionsWithAnswers(templateId)
    }

    suspend fun deleteEvent(event: Event) {
        localStorage.deleteEvent(event)
    }

    private enum class RepPrefs: PreferenceEntity {
        /**
         * day of last update values from remote. Need to get only new info
         * */
        DayOfLastUpdate{
            override val default: Long = 0
        }
    }



    companion object {
        /** prepare calendar to be used for db (left just date)
         * you must not use calendar object without this clearing
         * */
        fun clearDate(cal: Calendar): Calendar {
            cal[Calendar.HOUR_OF_DAY] = cal.getActualMinimum(Calendar.HOUR_OF_DAY)
            cal[Calendar.MINUTE] = cal.getActualMinimum(Calendar.MINUTE)
            cal[Calendar.SECOND] = cal.getActualMinimum(Calendar.SECOND)
            cal[Calendar.MILLISECOND] = cal.getActualMinimum(Calendar.MILLISECOND)
            return cal
        }
    }
}