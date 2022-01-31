package com.astery.xapplication.ui.activity.interfaces

import com.astery.xapplication.model.entities.ArticleTag
import com.astery.xapplication.ui.activity.popupDialogue.Blockable
import com.astery.xapplication.ui.activity.popupDialogue.DialogueHolder

interface FiltersUsable{
    fun setFilters(value:MutableList<ArticleTag>)
    fun getFilters():MutableList<ArticleTag>
    fun getBlockable():List<Blockable>
    fun getDialogueHolder():DialogueHolder
}