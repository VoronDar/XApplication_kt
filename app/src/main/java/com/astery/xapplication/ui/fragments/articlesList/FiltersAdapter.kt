package com.astery.xapplication.ui.fragments.articlesList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.astery.xapplication.databinding.UnitFilterBinding
import com.astery.xapplication.model.entities.ArticleTag
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder

class FiltersAdapter(units: List<ArticleTag>?, context: Context) :
    BaseAdapter<FiltersAdapter.ViewHolder, ArticleTag>(units, context) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = UnitFilterBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val binding = (h as ViewHolder).binding
        binding.tag = units!![position]
        binding.isOdd = position%2 == 1
    }

    override fun onViewDetachedFromWindow(h: BaseViewHolder) {
        super.onViewDetachedFromWindow(h)
        val binding = (h as ViewHolder).binding
        binding.unbind()
    }


    inner class ViewHolder(val binding: UnitFilterBinding) :
        BaseViewHolder(blockListener, binding.root)
}