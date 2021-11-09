package com.astery.xapplication.model.appValues

import androidx.room.TypeConverter
import com.astery.xapplication.model.entities.values.EventCategory
import java.lang.RuntimeException

class EventCategoryConverter {
    @TypeConverter
    fun toDb(value: EventCategory?): Int {
        return value?.ordinal?:EventCategory.FEELS.ordinal
    }

    @TypeConverter
    fun toClass(data: Int): EventCategory {
        for (e in EventCategory.values()) {
            if (e.ordinal == data) return e
        }
        throw RuntimeException("EventCategoryConverter got invalid enum ordinal = $data")
    }
}