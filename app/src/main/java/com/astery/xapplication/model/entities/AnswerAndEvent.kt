package com.astery.xapplication.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * events and answers have many to many relationship,
 * using this table you can understand how did the user answered the questions
 * */
@Entity
data class AnswerAndEvent(val eventId:Int, val answerId:Int){
    @PrimaryKey(autoGenerate = true) var id:Int? = null
}