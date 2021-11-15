package com.astery.xapplication.ui.fragments.advices

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.astery.xapplication.databinding.UnitAdviceBinding
import com.astery.xapplication.databinding.UnitAnswerWithAdvicesBinding
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder
import java.util.*

/**
 * advices for answers
 * */
class AdvicesAdapter(units: ArrayList<AdvicesUnit>?, context: Context) :
    BaseAdapter<AdvicesAdapter.ViewHolder, AdvicesUnit>(units, context) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = UnitAnswerWithAdvicesBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val binding = (h as ViewHolder).binding
        units!![position].also { unit ->
            binding.unit = unit

            binding.tipLayout.removeAllViews()
            if (unit.advices != null) {
                for (i in unit.advices) {
                    val adviceBinding = UnitAdviceBinding.inflate(LayoutInflater.from(context))
                    adviceBinding.advice = i
                    binding.tipLayout.addView(adviceBinding.root)
                }
            }
        }
    }

    override fun onViewDetachedFromWindow(h: BaseViewHolder) {
        super.onViewDetachedFromWindow(h)
        val binding = (h as ViewHolder).binding
        binding.unbind()
    }


    inner class ViewHolder(val binding: UnitAnswerWithAdvicesBinding) :
        BaseViewHolder(blockListener, binding.root)
}