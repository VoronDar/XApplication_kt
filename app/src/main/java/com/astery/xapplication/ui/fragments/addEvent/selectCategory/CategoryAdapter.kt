package com.astery.xapplication.ui.fragments.addEvent.selectCategory

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.astery.xapplication.R
import com.astery.xapplication.databinding.UnitCategoryBinding
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder
import com.astery.xapplication.ui.utils.getDraw
import java.util.ArrayList

/**
 *
 * */
class CategoryAdapter(units: ArrayList<EventCategory>, context: Context) :
    BaseAdapter<CategoryAdapter.ViewHolder, EventCategory>(units, context) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = UnitCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val binding = (h as ViewHolder).binding
        binding.image = context.getDraw(units!![position].getDrawableId())
        binding.title = context.resources.getStringArray(R.array.event_category)[position]

        binding.card.elevation = 8f
    }

    override fun onViewDetachedFromWindow(h: BaseViewHolder) {
        super.onViewDetachedFromWindow(h)
        val binding = (h as ViewHolder).binding
        binding.unbind()
    }


    inner class ViewHolder(val binding:UnitCategoryBinding) : BaseViewHolder(blockListener, binding.root) {}
}