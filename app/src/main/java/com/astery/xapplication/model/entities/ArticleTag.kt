package com.astery.xapplication.model.entities

import com.astery.xapplication.R
import timber.log.Timber

/**
 * tags that can be used to search articles
 * */

interface ArticleTag{
    /** each articleTag must have a unique code */
    val id:Int
    /** string resource id */
    val nameId:Int
    val iconId:Int


    companion object{
        fun convertToIdList(list:List<ArticleTag>):List<Int>{
            val result = ArrayList<Int>()
            for (i in list){
                result.add(i.id)
            }
            Timber.d("convert to id ${result}")
            return result
        }
        fun convertFromIdList(list:List<Int>):MutableList<ArticleTag>{
            Timber.d("convert from id ${list}")
            val result = ArrayList<ArticleTag>()
            for (i in ArticleTagType.values()){
                K@ for (k in i.getTagsForType()){
                    for (j in list){
                        if (j == k.id){
                            result.add(k)
                            continue@K
                        }
                    }
                }
            }
            return result
        }
    }
}

/** enum of types of tags. Using it you can get every tag*/
enum class ArticleTagType{
    Gender {
        override fun getTagsForType(): Array<ArticleTag> {
            return GenderTag.values() as Array<ArticleTag>
        }
        override fun getNameId(): Int = R.string.ar_tag_type_gender
    },
    Age {
        override fun getTagsForType(): Array<ArticleTag> {
            return AgeTag.values() as Array<ArticleTag>
        }
        override fun getNameId(): Int = R.string.ar_tag_type_age
    }
    /*,
    Topic {
        override fun getTagsForType(): Array<ArticleTag> {
            return TopicTag.values() as Array<ArticleTag>
        }

        override fun getNameId(): Int = R.string.ar_tag_type_topic
    } */;

    abstract fun getTagsForType():Array<ArticleTag>
    abstract fun getNameId():Int
}



enum class GenderTag:ArticleTag {
    Woman{
         override val id = 0
         override val nameId = R.string.ar_tag_gender_woman
            override val iconId = R.drawable.tag_woman
         },
    Man{
        override val id = 1
        override val nameId = R.string.ar_tag_gender_man
        override val iconId = R.drawable.tag_man
    };
}



enum class AgeTag:ArticleTag{
    Adult{
        override val id = 101
        override val nameId = R.string.ar_tag_age_adult
        override val iconId = R.drawable.tag_adult
    }, Teen{
        override val id = 102
        override val nameId = R.string.ar_tag_age_teen
        override val iconId = R.drawable.tag_teen
    },Child{
        override val id = 103
        override val nameId = R.string.ar_tag_age_child
        override val iconId = R.drawable.tag_child
    }
}

/* TODO(I'll make you later)
enum class TopicTag:ArticleTag{
    Health{
        override val id = 10001
        override val nameId = R.string.ar_tag_topic_health
        override val iconId = R.drawable.event_placeholder
    }, Relations{
        override val id = 10002
        override val nameId = R.string.ar_tag_topic_relations
        override val iconId = R.drawable.event_placeholder
    },Sex{
        override val id = 10003
        override val nameId = R.string.ar_tag_topic_sex
        override val iconId = R.drawable.event_placeholder
    }
}
 */