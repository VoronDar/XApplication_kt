package com.astery.xapplication

import android.graphics.Bitmap
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.jparams.verifier.tostring.NameStyle
import com.jparams.verifier.tostring.ToStringVerifier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.util.*


class TestEvent {

    private lateinit var event: Event

    @Before
    fun createEvent() {
        event = Event(
            TestHelper.getId(),
            TestHelper.getTemplateId(),
            TestHelper.getDate()
        )
    }

    @Test
    fun testEqualsWithItself() = assertEquals(event, event)

    @Test
    fun testEqualsWithCopy() = assertEquals(event, event.clone())


    @Test
    fun testEqualsWithInvalidDate() =
        assertNotEquals(event, event.clone(date = TestHelper.getInvalidDate()))

    @Test
    fun testEqualsWithInvalidId() = assertNotEquals(event, event.clone(id = TestHelper.getInvalidId()))

    @Test
    fun testEqualsWithInvalidTemplateId() =
        assertNotEquals(event, event.clone(templateId = TestHelper.getInvalidTemplateId()))


    @Test
    fun testToString(){
        ToStringVerifier.forClass(Event::class.java)
            .withClassName(NameStyle.SIMPLE_NAME)
            .verify()
    }


    object TestHelper {
        fun getDate() = Date(12123123123)

        fun getInvalidDate(): Date {
            val date = Date(141141421)
            date.time += 10
            return date
        }

        fun getId() = 1
        fun getInvalidId() = 2
        fun getTemplateId() = 1
        fun getInvalidTemplateId() = 2
    }

    private fun Event.clone(id:Int? = this.id, templateId:Int = this.templateId, date:Date = this.date,
                            image: Bitmap? = this.image, template: EventTemplate? = this.template):Event {
        val event = this.copy(id = id, templateId = templateId, date = Date(date.time))
        event.image = image
        // template doesn't need to be deeply copied
        event.template = template
        return event

    }
}

