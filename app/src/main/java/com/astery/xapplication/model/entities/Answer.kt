package com.astery.xapplication.model.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * any question has several answers. Some answers have an item, that has advices what to do in the situation
 */
@Parcelize
@Entity
data class Answer(
    @PrimaryKey var id: Int, val body: String,
    val itemId: Int?, @ColumnInfo(name = "parent_id") val questionId: Int
) :
    Parcelable {

    @Ignore
    var item: Item? = null


    @Ignore
    constructor() : this(0, "", 0, 0)

    override fun toString(): String {
        return "Answer(id=$id, body='$body', itemId=$itemId, questionId=$questionId, item=$item)"
    }
}