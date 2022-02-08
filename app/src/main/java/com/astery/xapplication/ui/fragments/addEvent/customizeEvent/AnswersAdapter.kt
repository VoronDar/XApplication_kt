package com.astery.xapplication.ui.fragments.addEvent.customizeEvent

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.astery.xapplication.databinding.UnitAnswerBinding
import com.astery.xapplication.model.entities.Answer
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder
import com.astery.xapplication.ui.adapterUtils.BlockListener

/**
 * answers for questions
 * */
class AnswersAdapter(units: List<Answer>?, context: Context, question: QuestionUnit) :
    BaseAdapter<AnswersAdapter.ViewHolder, Answer>(units, context) {

    override var blockListener: BlockListener? = object : BlockListener {
        override fun onClick(position: Int) {
            question.selectedAnswerPos = position
            selectedAnswer = position
        }

    }

    var selectedAnswer: Int = 0
        set(value) {
            //val oldValue = selectedAnswer
            field = value
            /*
            Timber.d("$oldValue, $value")
            notifyItemChanged(oldValue)
            notifyItemChanged(selectedAnswer)

             */
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = UnitAnswerBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val binding = (h as ViewHolder).binding
        units!![position].also { unit ->
            binding.answer = unit
            binding.isSelected = selectedAnswer == position
        }
    }

    override fun onViewDetachedFromWindow(h: BaseViewHolder) {
        super.onViewDetachedFromWindow(h)
        val binding = (h as ViewHolder).binding
        binding.unbind()
    }


    inner class ViewHolder(val binding: UnitAnswerBinding) :
        BaseViewHolder(blockListener, binding.root)
}