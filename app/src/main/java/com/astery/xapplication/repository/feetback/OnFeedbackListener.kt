package com.astery.xapplication.repository.feetback

import com.astery.xapplication.model.entities.FeedBackState

/** interface for handling like or dislike actions */
interface OnFeedbackListener{
    fun onLike()
    fun onCancelLike()
    fun onDislike()
    fun onCancelDislike()
    fun onChangeFeetBackState(feedBackState: FeedBackState)
}
