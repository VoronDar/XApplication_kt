package com.astery.xapplication.model.remote

import com.astery.xapplication.model.entities.AgeTag
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.ArticleTag
import com.astery.xapplication.model.entities.GenderTag
import com.astery.xapplication.repository.RemoteEntity


class ArticleFromRemote : Article(),
    RemoteEntity<Article> {
    override var lastUpdated: Int = 0

    var tagMan: Boolean = false
    var tagWoman: Boolean = false

    var tagAdult: Boolean = false
    var tagTeen: Boolean = false
    var tagChild: Boolean = false

    override fun convertFromRemote(): Article {
        val tags = ArrayList<ArticleTag>()
        if (tagMan) tags.add(GenderTag.Man)
        if (tagWoman) tags.add(GenderTag.Woman)

        if (tagAdult) tags.add(AgeTag.Adult)
        if (tagTeen) tags.add(AgeTag.Teen)
        if (tagChild) tags.add(AgeTag.Child)
        this.tags = tags
        return this
    }
}