package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.*
import com.astery.xapplication.model.entities.converters.ArrayConverter
import com.astery.xapplication.model.entities.converters.EventCategoryConverter
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.RemoteEntity
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * type of event. Can't be changed by a user.
 * Contains list of questions.
 */
@Parcelize
@Entity
@TypeConverters(ArrayConverter::class, EventCategoryConverter::class)
open class EventTemplate(
    @PrimaryKey var id: Int, var name: String,
    var body: String,
    @ColumnInfo(name = "event_category")
    val eventCategory: EventCategory? = EventCategory.Dating
) : Serializable, Parcelable{

    @Ignore
    var image: Bitmap? = null
    @Ignore
    var questions: List<Question>? = null


    @Ignore
    constructor() : this(0, "", "", EventCategory.Dating)

    @Ignore
    constructor(id:Int) : this(id, "", "", EventCategory.Dating)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventTemplate) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (body != other.body) return false
        if (eventCategory != other.eventCategory) return false
        if (questions != other.questions) return false

        return true
    }

    override fun toString(): String {
        return "EventTemplate(id=$id, name='$name', body='$body', eventCategory=$eventCategory, image=$image, questions=$questions)"
    }

}