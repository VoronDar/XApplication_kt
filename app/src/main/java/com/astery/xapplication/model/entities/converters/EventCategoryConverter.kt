package com.astery.xapplication.model.entities.converters

import androidx.room.TypeConverter
import com.astery.xapplication.model.entities.values.EventCategory
import java.lang.RuntimeException

class EventCategoryConverter {
    @TypeConverter
    fun toDb(value: EventCategory?): Int {
        return value?.ordinal?:EventCategory.Feels.ordinal
    }

    @TypeConverter
    fun toClass(data: Int): EventCategory {
        return EventCategory.values()[data]
    }
}