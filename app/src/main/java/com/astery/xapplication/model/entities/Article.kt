package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.astery.xapplication.model.entities.converters.ArrayConverter
import com.astery.xapplication.model.entities.converters.FeedbackStateConverter

/**
 * the hugest thing. Has several items. Can be found in article pool.
 *
 */
@Entity
@TypeConverters(ArrayConverter::class, FeedbackStateConverter::class)
data class Article(@PrimaryKey var id: Int, val name: String, val body: String?,
                   var likes:Int, var dislikes:Int, val feedBack: FeedBackState?=FeedBackState.None)
{
    @Ignore
    var tags: List<ArticleTag>? = null
    @Ignore
    var items: List<Item>? = null
    @Ignore
    var image: Bitmap? = null

    @Ignore
    var ta:String? = null


    @Ignore
    constructor():this(0, "", "", 0,0)

    fun addTags(tags:List<ArticleTag>):Article{
        this.tags = tags
        return this
    }

}
