package com.astery.xapplication.ui.pageFeetback.advice

import com.astery.xapplication.model.entities.FeedBackState

/** expansion of onFeetbackListener for advice (basic listener doesn't suit advices.
 * Because there are many advices for one view model)
 * It stores there because there are many fragments that have advices
 * */
    interface OnAdviceFeetbackListener{
        fun onLike(id:Int)
        fun onDislike(id:Int)
        fun onCancelLike(id:Int)
        fun onCancelDisLike(id:Int)
        fun onChangeFeetbackState(id:Int, feedBackState: FeedBackState)
    }