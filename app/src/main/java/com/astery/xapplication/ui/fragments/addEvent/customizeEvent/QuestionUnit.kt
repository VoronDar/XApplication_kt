package com.astery.xapplication.ui.fragments.addEvent.customizeEvent

import com.astery.xapplication.model.entities.Question

/** unit for questions used only in AddEventFragment.
 * You can't use just question model, because it don't have the
 * */
class QuestionUnit private constructor(val question: Question, val questionPos: Int) {
    var selectedAnswerPos = 0
        set(value) {
            question.selectedAnswer = question.answers!![value]
            field = value
        }

    companion object {
        fun createFromList(list: List<Question>): ArrayList<QuestionUnit> {
            val questions = ArrayList<QuestionUnit>()
            for (i in list) {
                questions.add(QuestionUnit(i, questions.size + 1))
            }
            return questions
        }
    }
}