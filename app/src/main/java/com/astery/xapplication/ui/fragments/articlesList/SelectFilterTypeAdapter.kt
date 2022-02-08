package com.astery.xapplication.ui.fragments.articlesList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.databinding.UnitSelectTagWithTypeBinding
import com.astery.xapplication.model.entities.ArticleTag
import com.astery.xapplication.model.entities.ArticleTagType
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder

class SelectFilterTypeAdapter(units: List<ArticleTagType>?, context: Context) :
    BaseAdapter<SelectFilterTypeAdapter.ViewHolder, ArticleTagType>(units, context) {
    var selectedTags: MutableList<ArticleTag> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = UnitSelectTagWithTypeBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val binding = (h as ViewHolder).binding
        binding.tagType = units!![position]

        val selectFiltersAdapter =
            SelectFilterAdapter(units!![position].getTagsForType().toList(), context, selectedTags)
        binding.recyclerView.adapter = selectFiltersAdapter
        // TODO(make as a grid)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.isNestedScrollingEnabled = false
        binding.recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

    }

    override fun onViewDetachedFromWindow(h: BaseViewHolder) {
        super.onViewDetachedFromWindow(h)
        val binding = (h as ViewHolder).binding
        binding.unbind()
    }


    inner class ViewHolder(val binding: UnitSelectTagWithTypeBinding) :
        BaseViewHolder(blockListener, binding.root)
}