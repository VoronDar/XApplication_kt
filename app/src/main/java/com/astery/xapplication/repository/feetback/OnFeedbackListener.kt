package com.astery.xapplication.repository.feetback

/** interface for handling like or dislike actions */
interface OnFeedbackListener {
    fun onLike()
    fun onCancelLike()
    fun onDislike()
    fun onCancelDislike()
}
