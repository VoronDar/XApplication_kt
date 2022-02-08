package com.astery.xapplication.ui.activity.interfaces

import com.astery.xapplication.model.entities.ArticleTag

interface FiltersUsable: PanelUsable{
    fun setFilters(value:MutableList<ArticleTag>)
    fun getFilters():MutableList<ArticleTag>
}