package com.astery.xapplication.model.entities.converters

import androidx.room.TypeConverter
import java.lang.StringBuilder
import java.util.*

class ArrayConverter {
    @TypeConverter
    fun toDb(value: List<String?>?): String? {
        if (value == null) return null
        val builder = StringBuilder()
        for (l in value) {
            builder.append(l).append(",")
        }
        return builder.substring(0, builder.length - 2)
    }

    @TypeConverter
    fun toClass(data: String?): List<String>? {
        return if (data == null) null else Arrays.asList(
            *data.split(",").toTypedArray()
        )
    }
}