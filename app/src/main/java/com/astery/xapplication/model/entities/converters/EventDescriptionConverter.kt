package com.astery.xapplication.model.entities.converters

import androidx.room.TypeConverter
import com.astery.xapplication.model.EventDescription
import java.util.*


class EventDescriptionConverter {
    @TypeConverter
    fun toDb(value: EventDescription?): String? {
        if (value?.properties == null) return null
        val properties = StringBuilder()
        for (k in value.properties.keys) {
            properties.append(k).append("|").append(value.properties.get(k)).append("//")
        }
        return properties.substring(0, properties.length - 2)
    }

    @TypeConverter
    fun toClass(data: String?): EventDescription? {
        if (data == null) return null
        val properties = ArrayList<String>()
        Collections.addAll(properties, *data.split("//").toTypedArray())
        val values: MutableMap<String, String> = HashMap()
        for (k in properties) {
            val key = k.substring(0, k.indexOf("|"))
            val value = k.substring(k.indexOf("|") + 1)
            values[key] = value
        }
        return EventDescription(values)
    }
}