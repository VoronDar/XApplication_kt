package com.astery.xapplication.ui.fragments.addEvent.customizeEvent

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.databinding.UnitQuestionBinding
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder

/**
 * advices for answers
 * */
class QuestionsAdapter(units: ArrayList<QuestionUnit>?, context: Context) :
    BaseAdapter<QuestionsAdapter.ViewHolder, QuestionUnit>(units, context) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = UnitQuestionBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding, null)
    }

    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val binding = (h as ViewHolder).binding
        units!![position].also { unit ->
            binding.question = unit
            if (h.answerAdapter == null)
                h.answerAdapter = AnswersAdapter(unit.question.answers!!, context, unit)
            binding.recyclerView.adapter = h.answerAdapter
            binding.recyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        }
    }

    override fun onViewDetachedFromWindow(h: BaseViewHolder) {
        super.onViewDetachedFromWindow(h)
        val binding = (h as ViewHolder).binding
        binding.unbind()
    }


    inner class ViewHolder(val binding: UnitQuestionBinding, var answerAdapter: AnswersAdapter?) :
        BaseViewHolder(blockListener, binding.root)
}