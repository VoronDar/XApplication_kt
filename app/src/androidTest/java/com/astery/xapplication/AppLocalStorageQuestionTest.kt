package com.astery.xapplication

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.astery.xapplication.model.entities.Answer
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.Question
import com.astery.xapplication.repository.localDataStorage.AppDatabase
import com.astery.xapplication.repository.localDataStorage.AppLocalStorage
import com.astery.xapplication.repository.localDataStorage.LocalStorage
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
class AppLocalStorageQuestionTest {

    private lateinit var localStorage: LocalStorage
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
            ).build(), instrumentationContext
        )
    }

    @After
    fun closeDb() {
        runBlocking {
            launch {
                localStorage.deleteEvents()
                localStorage.deleteQuestions()
                localStorage.deleteAnswers()
                localStorage.deleteEventTemplates()
                localStorage.close()
            }
        }
    }


    /**
     * add questions and selected answers to event (like we adding new event)
     * get selected answers. compare with actual
     * */
    @Test
    fun checkGetSelectedAnswersAndQuestions() {
        runBlocking {
            launch {
                localStorage.deleteEvents()
                localStorage.deleteAnswers()
                localStorage.deleteQuestions()

                val eventId = 1
                val templateId = 1

                val answers = listOf(
                    Answer(1, "answer1", 1, 1),
                    Answer(2, "answer2", null, 2),
                    Answer(3, "answer3", null, 3),
                    Answer(4, "answer4", 4, 4)
                )
                localStorage.addAnswers(answers)

                val questions = listOf(
                    Question(1, "fake question1", 1, answers[0]),
                    Question(2, "fake question2", 1, answers[1]),
                    Question(3, "fake question3", 1, answers[2]),
                    Question(4, "fake question4", 1, answers[3])
                )
                localStorage.addQuestions(questions)


                val template = EventTemplate(templateId, "eventTemplate", "body")
                template.questions = questions

                val event = Event(eventId, templateId, Date())
                event.template = template

                localStorage.addEvent(event)

                val newQuestions = localStorage.getQuestionsAndSelectedAnswersForEvent(eventId)

                assertTrue("got questions are incorrect", questions == newQuestions)
            }
        }
    }

    /**
     * like checkGetSelectedAnswersAndQuestions, but add everything to template
     * */
    @Test
    fun checkGetEventInfo() {
        runBlocking {
            launch {
                localStorage.deleteEvents()
                localStorage.deleteEventTemplates()
                localStorage.deleteAnswers()
                localStorage.deleteQuestions()

                val eventId = 213
                val templateId = 112
                val date = Date(121312312)

                val answers = listOf(
                    Answer(1, "answer1", 1, 1),
                    Answer(2, "answer2", null, 2),
                    Answer(3, "answer3", null, 3),
                    Answer(4, "answer4", 4, 4)
                )
                localStorage.addAnswers(answers)

                val questions = listOf(
                    Question(1, "fake question1", 1, answers[0]),
                    Question(2, "fake question2", 1, answers[1]),
                    Question(3, "fake question3", 1, answers[2]),
                    Question(4, "fake question4", 1, answers[3])
                )
                localStorage.addQuestions(questions)


                val template = EventTemplate(templateId, "eventTemplate", "body")
                template.questions = questions
                localStorage.addTemplate(template)

                val event = Event(eventId, templateId, date)
                event.template = template
                localStorage.addEvent(event)


                val newEvent = Event(eventId, templateId, date)
                newEvent.template = localStorage.getDescriptionForEvent(newEvent)
                assertTrue(
                    "newEvent is not equal with event\n\n newEvent = $newEvent, \n\nevent = $event}",
                    newEvent == event
                )
            }
        }
    }

}