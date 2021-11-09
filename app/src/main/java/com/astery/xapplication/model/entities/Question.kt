package com.astery.xapplication.model.entities

import androidx.room.*
import com.astery.xapplication.repository.remoteDataStorage.FbUsable

/**
 * narrow questions for desire and consequence that allow to know more
 */
/*
@Entity
data class Question(
    @PrimaryKey var id: String,
    var text: String,

    @ColumnInfo(name = "parent_id")
    var parentId: String? = null) : FbUsable {

    @Ignore
    var answers: List<Answer>? = null

    @Ignore
    var selectedAnswer: Answer? = null


    override fun setVariableId(id: String) {
        this.id = id
    }

    override fun toString(): String {
        return "Question{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", parentId='" + parentId + '\'' +
                ", answers=" + answers +
                ", selectedAnswer=" + selectedAnswer +
                '}'
    }
}

 */