package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import androidx.room.*
import com.astery.xapplication.model.appValues.ArrayConverter

/**
 * the hugest thing. Has several items. Can be found in article pool.
 */
@Entity
@TypeConverters(ArrayConverter::class)
data class Article(@PrimaryKey var id: String, var name: String, var description: String,
                   var likes:Int, var dislikes:Int,
                   @ColumnInfo(name = "wide_tags") var wideTags: List<String>?,
                   @ColumnInfo(name = "key_words") var keyWords: List<String>?)
{
    @Ignore
    private var items: List<Item>? = null
    @Ignore
    private var image: Bitmap? = null

    @Ignore
    constructor():this("", "", "", 0,0, null, null)

}