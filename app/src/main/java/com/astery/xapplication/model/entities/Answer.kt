package com.astery.xapplication.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * any question has several answers. Some answers have an item, that has advices what to do in the situation
 */
@Entity
data class Answer(@PrimaryKey var id: Int, val body: String,
                  val itemId: Int, @ColumnInfo(name = "parent_id") val questionId: String) {

    @Ignore var item: Item? = null


    @Ignore
    constructor():this(0, "", 0, "") {
    }


    override fun toString(): String {
        return "Answer{" +
                "id='" + id + '\'' +
                ", body='" + body + '\'' +
                ", itemId='" + itemId + '\'' +
                ", item=" + item +
                ", questionId='" + questionId + '\'' +
                '}'
    }

}