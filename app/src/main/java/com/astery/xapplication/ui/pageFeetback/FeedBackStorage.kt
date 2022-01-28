package com.astery.xapplication.ui.pageFeetback

import com.astery.xapplication.model.entities.FeedBackState
import com.astery.xapplication.repository.feetback.OnFeedbackListener

/** class used in dataBinding to change feetbackState */
class FeedBackStorage(
    var likes: Int,
    var dislikes: Int,
    var feedBackState: FeedBackState = FeedBackState.None,
    val listener: OnFeedbackListener = object:OnFeedbackListener{
        override fun onLike() {}
        override fun onCancelLike() {}
        override fun onDislike() {}
        override fun onCancelDislike() {}
        override fun onChangeFeetBackState(feedBackState: FeedBackState) {}
    }
) {

    /** called from xml (dataBinding) when press like or dislike.
     * feetBackbutton can be only like or dislike
     * change feetback state, call listener funtions
     * */
    fun press(feedBackButton: FeedBackState) {
        FeedBackState.press(this, feedBackButton)
    }
}