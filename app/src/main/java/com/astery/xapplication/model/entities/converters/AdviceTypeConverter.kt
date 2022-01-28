package com.astery.xapplication.model.entities.converters

import androidx.room.TypeConverter
import com.astery.xapplication.model.entities.values.AdviceType
import com.astery.xapplication.model.entities.values.EventCategory
import java.lang.RuntimeException

class AdviceTypeConverter {
    @TypeConverter
    fun toDb(value: AdviceType?): Int {
        return value?.ordinal?: AdviceType.Alert.ordinal
    }

    @TypeConverter
    fun toClass(data: Int): AdviceType {
        return AdviceType.values()[data]
    }
}