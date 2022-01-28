package com.astery.xapplication.repository.localDataStorage.dao

import androidx.room.*
import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.converters.AdviceTypeConverter
import com.astery.xapplication.model.entities.converters.DateConverter
import com.astery.xapplication.model.entities.converters.EventCategoryConverter
import com.astery.xapplication.model.entities.values.EventCategory
import java.util.*

@Dao
@TypeConverters(DateConverter::class, EventCategoryConverter::class, AdviceTypeConverter::class)
interface EventsDao {
    @Query("SELECT * FROM event WHERE date = :time")
    suspend fun getEventsByTime(time: Date): List<Event>

    @Query("SELECT * FROM eventtemplate WHERE id = :id")
    suspend fun getEventTemplate(id: Int): EventTemplate

    @Query("SELECT * FROM eventtemplate WHERE event_category = :category")
    suspend fun getEventTemplatesWithCategory(category: EventCategory): List<EventTemplate>

    @Query("SELECT * FROM eventtemplate")
    suspend fun getEventTemplatesWithCategory(): List<EventTemplate>

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getEvent(id: Int): Event

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEventTemplate(template: EventTemplate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEventTemplates(templates: List<EventTemplate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvent(event: Event):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvents(events: List<Event>)



    @Query("SELECT * from question WHERE eventTemplateId = :parentId")
    suspend fun getQuestionsForEventTemplate(parentId: Int): List<Question>

    @Query("SELECT * from question WHERE id = :id")
    suspend fun getQuestion(id: Int): Question

    /** get just answers */
    @Query("SELECT Answer.* from AnswerAndEvent inner Join Answer on eventId == :eventId AND Answer.id == answerId")
    suspend fun getAnswersForEvent(eventId:Int):List<Answer>

    /** get just answers id  */
    @Query("SELECT Answer.id from Answer WHERE Answer.parent_id == :questionId")
    suspend fun getAnswersIdForQuestion(questionId:Int):List<Int>

    /** get just answers */
    @Query("SELECT Answer.* from Answer WHERE Answer.parent_id == :questionId")
    suspend fun getAnswersForQuestion(questionId:Int):List<Answer>

    @Transaction
    suspend fun getAnswersAndQuestionsForEvent(eventId: Int):List<Question>{
        val questions:ArrayList<Question> = ArrayList()
        val list = getAnswersForEvent(eventId)
        for (i in list){
            questions.add(getQuestion(i.questionId))
            questions.last().selectedAnswer = i
        }
        return questions
    }

    @Transaction
    suspend fun getAnswersAndQuestionsForTemplate(templateId: Int):List<Question>{
        val questions = getQuestionsForEventTemplate(templateId)
        for (i in questions){
            i.answers = getAnswersForQuestion(i.id)
        }
        return questions
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAnswer(answer: Answer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAnswers(answers: List<Answer?>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAnswerAndEvent(relation: AnswerAndEvent)


    /** update answerId in AnswerAndEventRelation*/
    @Transaction
    suspend fun updateAnswerAndEvent(eventId: Int, question: Question){
        // get all answers for this question, find relation for one of these answer, replace
        val answers = getAnswersIdForQuestion(question.id)
        updateAnswerAndEvent(getAnswerAndEventRelation(eventId, answers)
            .copy(answerId = question.selectedAnswer!!.id))
    }

    /** get one relation for event and list of answers */
    @Query("SELECT * from ANSWERANDEVENT WHERE eventId == :eventId AND answerId in (:answersId)")
    suspend fun getAnswerAndEventRelation(eventId: Int, answersId: List<Int>):AnswerAndEvent

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAnswerAndEvent(relation: AnswerAndEvent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuestions(questions: List<Question?>)

    @Transaction
    suspend fun addQuestionsWithAnswers(questions:List<Question>){
        addQuestions(questions)
        for (i in questions){
            if (i.answers != null) addAnswers(i.answers!!)
        }
    }


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


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAnswer(answer: Answer?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAnswers(answers: List<Answer?>?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateQuestion(questions: Question?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateQuestions(questions: List<Question?>)

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

    @Query("DELETE from Event")
    suspend fun deleteEvents()

    @Query("DELETE from Event WHERE id == :id")
    suspend fun deleteEvent(id:Int)

    @Query("DELETE from AnswerAndEvent")
    suspend fun deleteAnswerAndEventRelations()

    @Query("DELETE from AnswerAndEvent WHERE eventId == :eventId")
    suspend fun deleteAnswerAndEventRelation(eventId: Int)

    @Query("DELETE from EventTemplate")
    suspend fun deleteEventTemplates()
    @Query("DELETE from EventTemplate WHERE id == :id")
    suspend fun deleteEventTemplate(id:String)

    @Query("DELETE from Answer")
    suspend fun deleteAnswers()
    @Query("DELETE from Question")
    suspend fun deleteQuestions()
}