package com.astery.xapplication.model.entities.converters

import androidx.room.TypeConverter
import com.astery.xapplication.model.entities.FeedBackState
import java.lang.RuntimeException

class FeedbackStateConverter {
    @TypeConverter
    fun toDb(value: FeedBackState?): Int {
        return value?.ordinal?: FeedBackState.None.ordinal
    }

    @TypeConverter
    fun toClass(data: Int): FeedBackState {
        for (e in FeedBackState.values()) {
            if (e.ordinal == data) return e
        }
        throw RuntimeException("FeedbackStateConverter got invalid enum ordinal = $data")
    }
}