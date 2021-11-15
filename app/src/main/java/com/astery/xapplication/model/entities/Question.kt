package com.astery.xapplication.model.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * narrow questions for desire and consequence that allow to know more
 */
@Parcelize
@Entity
data class Question(
    @PrimaryKey var id: Int,
    var body: String,
    var eventTemplateId: Int
) : Parcelable {

    @Ignore
    var answers: List<Answer>? = null

    @Ignore
    var selectedAnswer: Answer? = null

    @Ignore
    constructor(id: Int, body: String, eventTemplateId: Int, selectedAnswer: Answer) : this(
        id,
        body,
        eventTemplateId
    ) {
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