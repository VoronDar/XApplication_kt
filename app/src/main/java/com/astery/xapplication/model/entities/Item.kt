package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.astery.xapplication.repository.RemoteEntity
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * one item in article. Article may have many items (pages)
 * may has text, picture, and sets of advices.
 */
@Parcelize
@Entity
open class Item(
    @PrimaryKey open var id: Int = 0, var body: String,
    var name: String,
    @ColumnInfo(name = "parent_id") var articleId: Int?,
    /** position in article */
    val pagePosition: Int?
    //@ColumnInfo(name = "category_id") var categoryId: String
) : Parcelable{

    @Ignore
    var image: Bitmap? = null

    @Ignore
    var advices: List<Advice>? = null

    @Ignore
    constructor() : this(0, "", "", 0, 0)

    @Ignore
    constructor(id: Int) : this(id, "", "", 0, 0)


    fun clone(
        id: Int = this.id, body: String = this.body, name: String = this.name,
        articleId: Int? = this.articleId, pagePosition: Int? = this.pagePosition,
        image: Bitmap? = this.image, advices: List<Advice>? = this.advices
    ): Item {
        val item = Item(id, body, name, articleId, pagePosition)
        item.image = image
        item.advices = advices // i didn't forget to make deep copy, it works as expected
        return item
    }


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
