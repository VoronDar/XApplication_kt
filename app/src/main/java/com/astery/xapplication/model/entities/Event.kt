package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import androidx.room.*
import com.astery.xapplication.model.EventDescription
import com.astery.xapplication.model.appValues.ArrayConverter
import com.astery.xapplication.model.appValues.DateConverter
import com.astery.xapplication.model.appValues.EventDescriptionConverter
import java.util.*

@Entity
@TypeConverters(ArrayConverter::class, DateConverter::class, EventDescriptionConverter::class)
data class Event(@PrimaryKey var id: String,
                 @ColumnInfo(name = "template_id") var templateId: String,
                 @ColumnInfo(name = "description") var eventDescription: EventDescription? = null,
                 var date: Date?
){

    @Ignore private var bitmap: Bitmap? = null
    @Ignore var template: EventTemplate? = null

    @Ignore
    constructor():this("", "", null, null)
    /*
    val isTips: Boolean
        get() {
            if (template == null || template!!.questions == null) return false
            for (question in template!!.questions!!) {
                if (question.selectedAnswer.itemOb == null) continue
                return true
            }
            return false
        }

     */

    override fun toString(): String {
        return "Event{" +
                "id='" + id + '\'' +
                ", templateId='" + templateId + '\'' +
                ", eventDescription=" + eventDescription +
                ", date=" + date +
                ", bitmap=" + bitmap +
                ", template=" + template +
                '}'
    }
}