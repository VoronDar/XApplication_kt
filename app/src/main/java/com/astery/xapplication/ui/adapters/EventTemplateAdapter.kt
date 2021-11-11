package com.astery.xapplication.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.astery.xapplication.databinding.UnitEventBinding
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.ui.adapters.utils.BaseAdapter
import com.astery.xapplication.ui.adapters.utils.BaseViewHolder
import java.util.*

class EventTemplateAdapter(context: Context) :
    BaseAdapter<EventTemplateAdapter.ViewHolder, EventTemplate>(null, context) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = UnitEventBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val binding = (h as ViewHolder).binding
        binding.title = units!![position].name
        binding.description = units!![position].description
    }

    override fun onViewDetachedFromWindow(h: BaseViewHolder) {
        super.onViewDetachedFromWindow(h)
        val binding = (h as ViewHolder).binding
        binding.unbind()
    }


    inner class ViewHolder(val binding:UnitEventBinding) : BaseViewHolder(blockListener, binding.root)
}