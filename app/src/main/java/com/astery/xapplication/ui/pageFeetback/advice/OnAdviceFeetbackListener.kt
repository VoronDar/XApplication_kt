package com.astery.xapplication.ui.pageFeetback.advice

import com.astery.xapplication.model.entities.FeedBackState

/** expansion of onFeetbackListener for advice (basic listener doesn't suit advices.
 * Because there are many advices for one view model)
 * It stores there because there are many fragments that have advices
 * */
    interface OnAdviceFeetbackListener{
        fun onLike(id:Int, nowLikes:Int, nowDislikes:Int)
        fun onDislike(id:Int, nowLikes:Int, nowDislikes:Int)
        fun onCancelLike(id:Int, nowLikes:Int, nowDislikes:Int)
        fun onCancelDisLike(id:Int, nowLikes:Int, nowDislikes:Int)
    }