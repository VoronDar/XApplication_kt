package com.astery.xapplication.model.appValues

import androidx.room.TypeConverter
import com.astery.xapplication.model.entities.values.WarningCategory

class WarningCategoryConverter {
    @TypeConverter
    fun toDb(value: WarningCategory?): Int {
        return value?.ordinal ?: WarningCategory.WARNING.ordinal
    }

    @TypeConverter
    fun toClass(data: Int): WarningCategory {
        for (e in WarningCategory.values()) {
            if (e.ordinal == data) return e
        }
        throw RuntimeException("WarningCategoryConverter got invalid enum ordinal = $data")
    }
}