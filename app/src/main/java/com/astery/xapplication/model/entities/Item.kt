package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import androidx.room.*

/**
 * one item in article. Article may have many items (pages)
 * may has text, picture, and sets of advices.
 */
@Entity
data class Item(@PrimaryKey var id:Int, var body: String,
                var name: String,
                @ColumnInfo(name = "parent_id") var articleId: Int,
                /** position in article */
                val pagePosition:Int
                //@ColumnInfo(name = "category_id") var categoryId: String
                ) {

    @Ignore
    var image: Bitmap? = null

    @Ignore
    var advices: List<Advice>? = null

    @Ignore
    constructor():this(0, "", "", 0, 0)

    @Ignore
    constructor(id:Int):this(id, "", "", 0, 0)

    override fun toString(): String {
        return "Item{" +
                "id='" + id + '\'' +
                ", image=" + image +
                ", text='" + body + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + articleId + '\'' +
                //", categoryId='" + categoryId + '\'' +
                ", advises=" + advices +
                '}'
    }
}