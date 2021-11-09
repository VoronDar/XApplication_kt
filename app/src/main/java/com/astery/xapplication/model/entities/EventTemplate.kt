package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import androidx.room.*
import com.astery.xapplication.model.appValues.ArrayConverter
import com.astery.xapplication.model.appValues.EventCategoryConverter
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.remoteDataStorage.FbUsable

/**
 * used for cases when a user completed something and looking for a feedback
 */
@Entity
@TypeConverters(ArrayConverter::class, EventCategoryConverter::class)
data class EventTemplate(@PrimaryKey var id: String, var name: String,
                         var description: String,
                         @ColumnInfo(name = "event_category")
                         var eventCategory: EventCategory? = null) : FbUsable {

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
}