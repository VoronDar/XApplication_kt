package com.astery.xapplication.ui.fragments.articlesList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.astery.xapplication.databinding.UnitSelectFilterBinding
import com.astery.xapplication.model.entities.ArticleTag
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder

class SelectFilterAdapter(
    units: List<ArticleTag>?,
    context: Context,
    private val selectedTags: MutableList<ArticleTag>
) :
    BaseAdapter<SelectFilterAdapter.ViewHolder, ArticleTag>(units, context) {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = UnitSelectFilterBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {


        val binding = (h as ViewHolder).binding
        val unit = units!![position]
        val isSelected = selectedTags.contains(units!![position])
        binding.tag = unit
        binding.drawable = ContextCompat.getDrawable(context, unit.iconId)
        binding.isSelected = isSelected

        binding.root.setOnClickListener {
            if (isSelected) selectedTags.remove(unit)
            else selectedTags.add(unit)
            //
            //notifyDataSetChanged()
            notifyItemChanged(position)
        }
    }

    override fun onViewDetachedFromWindow(h: BaseViewHolder) {
        super.onViewDetachedFromWindow(h)
        val binding = (h as ViewHolder).binding
        binding.unbind()
    }


    inner class ViewHolder(val binding: UnitSelectFilterBinding) :
        BaseViewHolder(blockListener, binding.root)

}