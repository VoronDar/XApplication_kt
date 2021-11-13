package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import androidx.room.*
import com.astery.xapplication.model.entities.converters.ArrayConverter
import com.astery.xapplication.model.entities.converters.EventCategoryConverter
import com.astery.xapplication.model.entities.values.EventCategory
import java.io.Serializable

/**
 * type of event. Can't be changed by a user.
 * Contains list of questions.
 */
@Entity
@TypeConverters(ArrayConverter::class, EventCategoryConverter::class)
open class EventTemplate(@PrimaryKey var id: Int, var name: String,
                         var body: String,
                         @ColumnInfo(name = "event_category")
                         var eventCategory: EventCategory? = null): Serializable {

    @Ignore var image: Bitmap? = null
    //@Ignore var questions: List<Question>? = null


    @Ignore constructor():this(0, "", "", null)


    override fun toString(): String {
        return "EventTemplate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + body + '\'' +
                ", image=" + image +
                ", eventCategory=" + eventCategory +
                //", questions=" + questions +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventTemplate) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (body != other.body) return false
        if (eventCategory != other.eventCategory) return false
        if (image != other.image) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + (eventCategory?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        return result
    }

}