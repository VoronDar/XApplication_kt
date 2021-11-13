package com.astery.xapplication.ui.fragments.article

import android.graphics.Bitmap
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.Item

/** decorator for item and article */
    sealed class Presentable{
        abstract val name:String
        abstract val description:String?
        abstract val image: Bitmap?
    }
    class ArticlePresentable(val article: Article): Presentable() {
        override val name = article.name
        override val description = article.body
        override val image = article.image
    }
    
    class ItemPresentable(val item: Item): Presentable() {
        override val name = item.name
        override val description = item.body
        override val image = item.image
        val advices = item.advices
    }

