package com.astery.xapplication.repository.localDataStorage.dao

import androidx.room.*
import com.astery.xapplication.model.appValues.DateConverter
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import java.util.*

@Dao
@TypeConverters(DateConverter::class)
interface EventsDao {
    @Query("SELECT * FROM event WHERE date = :time")
    suspend fun getEventsByTime(time: Date): List<Event>

    @Query("SELECT * FROM eventtemplate WHERE id = :id")
    suspend fun getEventTemplate(id: String): EventTemplate

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getEvent(id: String): Event

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEventTemplate(template: EventTemplate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEventTemplates(templates: List<EventTemplate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvents(events: List<Event>)

    @Query("DELETE from Event")
    suspend fun deleteEvents()
    @Query("DELETE from Event WHERE id == :id")
    suspend fun deleteEvent(id:String)

    @Query("DELETE from EventTemplate")
    suspend fun deleteEventTemplates()
    @Query("DELETE from EventTemplate WHERE id == :id")
    suspend fun deleteEventTemplate(id:String)

    /*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWarningTemplate(template: WarningTemplate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWarningTemplates(templates: List<WarningTemplate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWarning(warning: Warning)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWarnings(warning: List<Warning>)

     */

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEventTemplate(template: EventTemplate)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEventTemplates(templates: List<EventTemplate>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEvent(event: Event)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEvents(events: List<Event>)

    /*
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWarningTemplate(template: WarningTemplate)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWarningTemplates(templates: List<WarningTemplate>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWarning(warning: Warning)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWarnings(warning: List<Warning>)

     */
}