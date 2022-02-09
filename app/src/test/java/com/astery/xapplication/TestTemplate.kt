package com.astery.xapplication

import android.graphics.Bitmap
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.Question
import com.astery.xapplication.model.entities.values.EventCategory
import com.jparams.verifier.tostring.NameStyle
import com.jparams.verifier.tostring.ToStringVerifier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class TestTemplate {
    private lateinit var template: EventTemplate

    @Before
    fun createEvent() {
        template = EventTemplate(
            TestHelper.getId(),
            TestHelper.getName(),
            TestHelper.getBody(),
            TestHelper.getEventCategory()
        )
        template.questions = TestHelper.getQuestions()
    }

    @Test
    fun testEqualsWithItself() = assertEquals(template, template)

    @Test
    fun testEqualsWithCopy() = assertEquals(template, template.clone())


    @Test
    fun testEqualsWithInvalidId() =
        assertNotEquals(template, template.clone(id = TestHelper.getInvalidId()))

    @Test
    fun testEqualsWithInvalidName() =
        assertNotEquals(template, template.clone(name = TestHelper.getInvalidName()))

    @Test
    fun testEqualsWithInvalidBody() =
        assertNotEquals(template, template.clone(body = TestHelper.getInvalidBody()))

    @Test
    fun testEqualsWithInvalidCategory() =
        assertNotEquals(
            template,
            template.clone(eventCategory = TestHelper.getInvalidEventCategory())
        )

    @Test
    fun testEqualsWithInvalidQuestions() =
        assertNotEquals(template, template.clone(questions = TestHelper.getInvalidQuestions()))

    @Test
    fun testToString() {
        ToStringVerifier.forClass(EventTemplate::class.java)
            .withClassName(NameStyle.SIMPLE_NAME)
            .verify()
    }


    object TestHelper {
        fun getId(): Int = 1
        fun getInvalidId(): Int = 2
        fun getEventCategory() = EventCategory.Dating
        fun getInvalidEventCategory() = EventCategory.Medicine
        fun getName() = "name"
        fun getInvalidName() = "invalid"
        fun getBody() = "body"
        fun getInvalidBody() = "invalid"
        fun getQuestions(): List<Question> = listOf()
        fun getInvalidQuestions(): List<Question>? = null
    }

    private fun EventTemplate.clone(
        id: Int = this.id,
        name: String = this.name,
        body: String = this.body,
        eventCategory: EventCategory = this.eventCategory!!,
        questions: List<Question>? = this.questions,
        image: Bitmap? = this.image
    ): EventTemplate {
        val template = EventTemplate(id, name, body, eventCategory)
        template.questions = questions
        template.image = image
        return template
    }


}

