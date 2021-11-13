package com.astery.xapplication.model.entities

import com.astery.xapplication.R

/**
 * tags that can be used to search articles
 * */
interface ArticleTag{
    /** each articleTag must have a unique code */
    val id:Int
    /** string resource id */
    val nameId:Int
}

enum class GenderTag:ArticleTag {
    Woman{
         override val id = 0
         override val nameId = R.string.title_new_event
         },
    Man{
        override val id = 1
        override val nameId = R.string.title_new_event
    };
}



enum class AgeTag:ArticleTag{
    Adult{
        override val id = 2
        override val nameId = R.string.title_new_event
    },Child{
        override val id = 3
        override val nameId = R.string.title_new_event
    }
}
