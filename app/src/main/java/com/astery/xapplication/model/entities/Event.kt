package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.*
import com.astery.xapplication.model.entities.converters.ArrayConverter
import com.astery.xapplication.model.entities.converters.DateConverter
import com.astery.xapplication.model.entities.converters.EventDescriptionConverter
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Events are used in calendarFragment. Always have templateId.
 * Events store dynamic info, but EventTemplates store static
 * */
@Parcelize
@Entity
@TypeConverters(ArrayConverter::class, DateConverter::class, EventDescriptionConverter::class)
data class Event(@PrimaryKey(autoGenerate = true) var id: Int?,
                 @ColumnInfo(name = "template_id") val templateId: Int,
                 val date: Date
): Parcelable{
    @Ignore var image:Bitmap? = null
    @Ignore var template: EventTemplate? = null


    /**
     * is there some advices for this event
     * depends on selected answers
     * */
    val isAdvices: Boolean
        get() {
            if (template == null || template!!.questions == null) return false
            for (question in template!!.questions!!) {
                if (question.selectedAnswer?.itemId == null) continue
                return true
            }
            return false

        }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Event) return false

        if (id != other.id) return false
        if (templateId != other.templateId) return false
        if (date != other.date) return false
        if (template != other.template) return false
        if (isAdvices != other.isAdvices) return false

        return true
    }

    override fun toString(): String {
        return "Event(id=$id, templateId=$templateId, date=$date, image=$image, template=$template, isAdvices=$isAdvices)"
    }


}