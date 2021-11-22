package com.astery.xapplication.ui.fragments.advices

import com.astery.xapplication.model.entities.Advice
import com.astery.xapplication.model.entities.Question

/** unit for AdviceAdapter.
 * order - position in view
 * position - the place of corresponding question in list
 * */
class AdvicesUnit private constructor(q: Question, val order:Int, val position:Int){
    val question:String = q.body
    val answer:String = q.selectedAnswer!!.body
    val advices:List<Advice>? = q.selectedAnswer!!.item!!.advices


    companion object{
        /** you can't create adviceUnit for question if there are no selectedAnswer for the question
         *  or item for this answer */
        fun create(question: Question, order: Int, position: Int): AdvicesUnit?{
            if (question.selectedAnswer != null && question.selectedAnswer!!.item != null){
                return AdvicesUnit(question, order, position)
            }
            return null
        }
        /**
         * transform list of questions to list of adviceUnits
         * */
        fun createList(list: List<Question>):List<AdvicesUnit>{
            val units = ArrayList<AdvicesUnit>()
            for (i in list.indices){
                val unit = create(list[i], units.size+1, i)
                if (unit != null) units.add(unit)
            }
            return units
        }
    }
}