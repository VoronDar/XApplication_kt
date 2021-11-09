package com.astery.xapplication.model.entities

import androidx.room.*
import com.astery.xapplication.model.appValues.ArrayConverter

/**
 * used for cases when a user wants to do something and asks for advises.
 */
/*
@Entity
@TypeConverters(ArrayConverter::class)
data class Desire(@PrimaryKey var id: String, var text: String,
    @ColumnInfo(name = "key_words") var keyWords: List<String>?
) {
    @Ignore
    var questions: List<Question>? = null
}

 */