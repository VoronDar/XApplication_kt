package com.astery.xapplication.model.remote

import com.astery.xapplication.model.entities.Question
import com.astery.xapplication.repository.RemoteEntity

class QuestionFromRemote : Question(0, "", 0), RemoteEntity<Question> {
    override var lastUpdated: Int = 0
    override fun convertFromRemote(): Question { return this
    }
}