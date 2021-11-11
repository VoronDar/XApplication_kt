package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.astery.xapplication.model.appValues.ArrayConverter
import com.astery.xapplication.model.appValues.EventCategoryConverter
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.remoteDataStorage.FbUsable
import java.io.Serializable
import java.util.*

/**
 * used for cases when a user completed something and looking for a feedback
 */
@Entity
@TypeConverters(ArrayConverter::class, EventCategoryConverter::class)
open class EventTemplate(@PrimaryKey var id: String, var name: String,
                         var description: String,
                         @ColumnInfo(name = "event_category")
                         var eventCategory: EventCategory? = null) : FbUsable, Serializable {

    @Ignore var image: Bitmap? = null
    //@Ignore var questions: List<Question>? = null


    @Ignore constructor():this("", "", "", null)



    override fun setVariableId(id: String) {
        this.id = id
    }

    override fun toString(): String {
        return "EventTemplate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
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
        if (description != other.description) return false
        if (eventCategory != other.eventCategory) return false
        if (image != other.image) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (eventCategory?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        return result
    }

}