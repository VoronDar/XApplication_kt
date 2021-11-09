package com.astery.xapplication.model.appValues

import androidx.room.TypeConverter
import com.astery.xapplication.model.entities.values.Appeared

class AppearedConverter {
    @TypeConverter
    fun toDb(value: Appeared?): Int {
        return value?.ordinal ?: Appeared.PREPARED.ordinal
    }

    @TypeConverter
    fun toClass(data: Int): Appeared {
        for (e in Appeared.values()) {
            if (e.ordinal == data) return e
        }
        throw RuntimeException("AppearedConverter got invalid enum ordinal = $data")
    }
}