package com.astery.xapplication
import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.repository.localDataStorage.AppDatabase
import com.astery.xapplication.repository.localDataStorage.LocalStorage
import com.astery.xapplication.repository.localDataStorage.AppLocalStorage
import com.astery.xapplication.roomEventsHelper.RoomEventsHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


/**
 * check RoomLocalStorage
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppLocalStorageEventsTest {


    lateinit var localStorage: LocalStorage
    private lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    @Throws(Exception::class)
    fun createDb() {
        localStorage = AppLocalStorage(
            Room.inMemoryDatabaseBuilder(
                instrumentationContext,
            AppDatabase::class.java
        ).build(), instrumentationContext)
    }

    @After
    fun closeDb(){
        runBlocking {
            launch {
                localStorage.deleteEventTemplates()
                localStorage.deleteEvents()
                localStorage.close()
            }
        }
    }


    /**
     * check EventsDao.getEventsByTime() and EventsDao.addEvent()
     * */
    @Test
    fun checkGetEventForTime(){
        runBlocking {
            launch {
                localStorage.deleteEvents()

                val requiredEvents = 2
                val now = Repository.clearDate(Calendar.getInstance())
                val anotherTime = Repository.clearDate(Calendar.getInstance())
                anotherTime.add(4, Calendar.DAY_OF_MONTH)

                localStorage.addEvent(RoomEventsHelper.getEmptyEvent(1, now.time))
                localStorage.addEvent(RoomEventsHelper.getEmptyEvent(2, now.time))
                localStorage.addEvent(RoomEventsHelper.getEmptyEvent(3, anotherTime.time))

                val new = localStorage.getEventsForDate(now)
                assertTrue("got invalid amount of events for date - ${new.size}", (new.size == requiredEvents))
                assertTrue("got incorrect events", (new[0] == RoomEventsHelper.getEmptyEvent(1, now.time)))
                assertTrue("got incorrect events", (new[1] == RoomEventsHelper.getEmptyEvent(2, now.time)))

            }
        }
    }

    /**
     * check add and get template with id
     * */
    @Test
    fun checkAddTemplate(){
        runBlocking {
            launch{
                localStorage.deleteEventTemplates()
                val id = 1
                val template = RoomEventsHelper.getEmptyTemplate(id)
                localStorage.addTemplate(template)
                assertTrue("got different event template", localStorage.getTemplate(id) == template)
            }
        }
    }

    /**
     *  add event, add template, get events with template
     * */
    @Test
    fun checkGetEventsWithTemplate(){
        runBlocking {
            launch {
                localStorage.deleteEventTemplates()
                localStorage.deleteEvents()

                val id = 1
                val event = RoomEventsHelper.getEmptyEvent(1, Date())
                event.template = RoomEventsHelper.getEmptyTemplate(id)
                TODO("event.templateId = event.templateId")

                localStorage.addTemplate(event.template!!)
                localStorage.addEvent(event)

                val newEvent = localStorage.getEvent(1)
                newEvent.template = localStorage.getTemplate(newEvent.templateId)

                assertTrue("got different template", event.template == newEvent.template)
                assertTrue("got different event", event == newEvent)

            }
        }
    }

}