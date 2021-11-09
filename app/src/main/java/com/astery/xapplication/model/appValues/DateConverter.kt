package com.astery.xapplication.model.appValues

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun toDb(value: Date): Long {
        return value.time
    }

    @TypeConverter
    fun toClass(data: Long): Date {
        return Date(data)
    }
}