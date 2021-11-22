package com.astery.xapplication.model.entities

import com.astery.xapplication.repository.feetback.OnFeedbackListener
import com.astery.xapplication.ui.pageFeetback.FeedBackStorage

enum class FeedBackState {
    Like {
        override fun pressOnMe(
            listener: OnFeedbackListener,
            likesDislikes: Pair<Int, Int>
        ): Pair<Int, Int> {
            listener.onLike()
            return Pair(likesDislikes.first + 1, likesDislikes.second)
        }

        override fun unPress(
            listener: OnFeedbackListener,
            likesDislikes: Pair<Int, Int>
        ): Pair<Int, Int> {
            listener.onCancelLike()
            return Pair(likesDislikes.first - 1, likesDislikes.second)
        }
    },
    Dislike {
        override fun pressOnMe(
            listener: OnFeedbackListener,
            likesDislikes: Pair<Int, Int>
        ): Pair<Int, Int> {
            listener.onDislike()
            return Pair(likesDislikes.first, likesDislikes.second + 1)
        }

        override fun unPress(
            listener: OnFeedbackListener,
            likesDislikes: Pair<Int, Int>
        ): Pair<Int, Int> {
            listener.onCancelDislike()
            return Pair(likesDislikes.first, likesDislikes.second - 1)
        }
    },
    None;

    protected open fun pressOnMe(listener: OnFeedbackListener, likesDislikes: Pair<Int, Int>) =
        likesDislikes

    protected open fun unPress(listener: OnFeedbackListener, likesDislikes: Pair<Int, Int>) =
        likesDislikes

    companion object {

        /** should be called only from FeetBackStorage
         * */
        fun press(
            storage: FeedBackStorage,
            feedBackButton: FeedBackState,
        ) {
            var likesDislikes = storage.feedBackState.unPress(
                storage.listener,
                Pair(storage.likes, storage.dislikes)
            )
            storage.feedBackState = if (feedBackButton == storage.feedBackState)
                None
            else
                feedBackButton
            likesDislikes = storage.feedBackState.pressOnMe(storage.listener, likesDislikes)
            storage.likes = likesDislikes.first
            storage.dislikes = likesDislikes.second
            storage.listener.onChangeFeetBackState(storage.feedBackState)
        }
    }
}