package com.astery.xapplication.model.entities.converters

import androidx.room.TypeConverter
import com.astery.xapplication.model.entities.values.WarningAppearState

class AppearedConverter {
    @TypeConverter
    fun toDb(value: WarningAppearState?): Int {
        return value?.ordinal ?: WarningAppearState.PREPARED.ordinal
    }

    @TypeConverter
    fun toClass(data: Int): WarningAppearState {
        for (e in WarningAppearState.values()) {
            if (e.ordinal == data) return e
        }
        throw RuntimeException("AppearedConverter got invalid enum ordinal = $data")
    }
}