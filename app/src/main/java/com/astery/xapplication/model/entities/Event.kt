package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import androidx.room.*
import com.astery.xapplication.model.EventDescription
import com.astery.xapplication.model.entities.converters.ArrayConverter
import com.astery.xapplication.model.entities.converters.DateConverter
import com.astery.xapplication.model.entities.converters.EventDescriptionConverter
import java.util.*

/**
 * Events are used in calendarFragment. Always have templateId.
 * Events store dynamic info, but EventTemplates store static
 * */
@Entity
@TypeConverters(ArrayConverter::class, DateConverter::class, EventDescriptionConverter::class)
data class Event(@PrimaryKey(autoGenerate = true) var id: Int?,
                 @ColumnInfo(name = "template_id") val templateId: Int,
                 @ColumnInfo(name = "description") var eventDescription: EventDescription? = null,
                 val date: Date
){
    @Ignore var image:Bitmap? = null
    @Ignore var template: EventTemplate? = null

    /**
     * is there some advices for this event
     * depends on eventDescription
     * */
    val isAdvices: Boolean
        get() {
            return false
            /* TODO(tips)
            if (template == null || template!!.questions == null) return false
            for (question in template!!.questions!!) {
                if (question.selectedAnswer.itemOb == null) continue
                return true
            }
            return false

             */
        }


    override fun toString(): String {
        return "Event{" +
                "id='" + id + '\'' +
                ", templateId='" + templateId + '\'' +
                ", eventDescription=" + eventDescription +
                ", date=" + date +
                ", template=" + template +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Event) return false

        if (id != other.id) return false
        if (templateId != other.templateId) return false
        if (eventDescription != other.eventDescription) return false
        if (date != other.date) return false
        if (image != other.image) return false
        if (template != other.template) return false

        return true
    }

}