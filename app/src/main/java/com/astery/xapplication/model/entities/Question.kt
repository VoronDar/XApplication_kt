package com.astery.xapplication.model.entities

import androidx.room.*
import com.astery.xapplication.repository.remoteDataStorage.FbUsable

/**
 * narrow questions for desire and consequence that allow to know more
 */
@Entity
data class Question(
    @PrimaryKey var id: Int,
    var body: String,
    var eventTemplateId: Int) {

    @Ignore
    var answers: List<Answer>? = null

    @Ignore
    var selectedAnswer: Answer? = null

    @Ignore
    constructor(id:Int, body: String, eventTemplateId: Int, selectedAnswer: Answer):this(id, body, eventTemplateId){
        this.selectedAnswer = selectedAnswer
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Question) return false

        if (id != other.id) return false
        if (body != other.body) return false
        if (eventTemplateId != other.eventTemplateId) return false
        if (answers != other.answers) return false
        if (selectedAnswer != other.selectedAnswer) return false

        return true
    }

    override fun toString(): String {
        return "Question(id=$id, body='$body', eventTemplateId=$eventTemplateId, answers=$answers, selectedAnswer=$selectedAnswer)"
    }


}