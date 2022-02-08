package com.astery.xapplication.model.entities.converters

import androidx.room.TypeConverter
import com.astery.xapplication.model.entities.FeedBackState

class FeedbackStateConverter {
    @TypeConverter
    fun toDb(value: FeedBackState?): Int {
        return value?.ordinal ?: FeedBackState.None.ordinal
    }

    @TypeConverter
    fun toClass(data: Int): FeedBackState {
        return FeedBackState.values()[data]
    }
}