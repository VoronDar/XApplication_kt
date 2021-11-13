package com.astery.xapplication.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * article and tag have many to many relationship,
 * using this table you can search articles with particular tags
 * */
@Entity
data class ArticleAndTag(val articleId:Int, val tagId:Int){
    @PrimaryKey(autoGenerate = true) var id:Int? = null
}