package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.*
import com.astery.xapplication.model.entities.converters.ArrayConverter
import com.astery.xapplication.model.entities.converters.FeedbackStateConverter
import kotlinx.parcelize.Parcelize
import timber.log.Timber

/**
 * the hugest thing. Has several items. Can be found in article pool.
 *
 */
@Parcelize
@Entity
@TypeConverters(ArrayConverter::class, FeedbackStateConverter::class)
open class Article(
    @PrimaryKey var id: Int, val name: String, val body: String?,
    var likes: Int = 0, var dislikes: Int = 0, val feedBack: FeedBackState = FeedBackState.None
) :
    Parcelable {
    @Ignore
    var tags: List<ArticleTag>? = null

    @Ignore
    var items: List<Item>? = null

    @Ignore
    var image: Bitmap? = null
        set(value) {
            if (id == 100) {
                Timber.d("THEY ARE CHANGED MY BITMAP $image")
            }
            field = value
        }

    @Ignore
    var ta: String? = null

    init {
        // TODO(Sometimes likes or dislikes are less, than zero. I don't know exactly how to handle it. But I made this to make it less visible)
        if (likes < 0) likes = 0
        if (dislikes < 0) dislikes = 0
    }


    @Ignore
    constructor() : this(0, "", "", 0, 0)

    fun addTags(tags: List<ArticleTag>): Article {
        this.tags = tags
        return this
    }

    override fun toString(): String {
        return "Article(id=$id, name='$name', body=$body, likes=$likes, dislikes=$dislikes, feedBack=$feedBack, bitmap=$image)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Article) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (body != other.body) return false
        if (likes != other.likes) return false
        if (dislikes != other.dislikes) return false
        if (feedBack != other.feedBack) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + likes
        result = 31 * result + dislikes
        result = 31 * result + feedBack.hashCode()
        return result
    }

}

@Entity
@Fts4(contentEntity = Article::class)
data class ArticleFts(val name: String, val body: String?)