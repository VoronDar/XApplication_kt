package com.astery.xapplication.ui.fragments.addEvent.selectTemplate

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.astery.xapplication.databinding.UnitEventBinding
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder
import timber.log.Timber

class EventTemplateAdapter(context: Context) :
    BaseAdapter<EventTemplateAdapter.ViewHolder, EventTemplate>(null, context) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = UnitEventBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val binding = (h as ViewHolder).binding
        val unit = units!![position]
        binding.title = unit.name
        binding.description = unit.body
        if (unit.image != null) {
            binding.image.isVisible = true
            binding.image.setImageBitmap(unit.image!!)
        }
    }

    override fun onViewDetachedFromWindow(h: BaseViewHolder) {
        super.onViewDetachedFromWindow(h)
        val binding = (h as ViewHolder).binding
        binding.unbind()
    }


    inner class ViewHolder(val binding:UnitEventBinding) : BaseViewHolder(blockListener, binding.root)
}