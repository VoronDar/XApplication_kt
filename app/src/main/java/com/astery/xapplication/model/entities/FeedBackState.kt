package com.astery.xapplication.model.entities

import com.astery.xapplication.repository.feetback.OnFeedbackListener
import com.astery.xapplication.ui.pageFeetback.FeedBackStorage
import timber.log.Timber

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
            var likesDislikes = Pair(storage.likes, storage.dislikes)


            val oldState = storage.feedBackState

            storage.feedBackState = if (feedBackButton == storage.feedBackState) {
                None
            }
            else
                feedBackButton

            Timber.d("storage ${storage.feedBackState}, old state - $oldState")

            when(storage.feedBackState){
                // если сейчас ничего не нажато
                None -> {
                    likesDislikes = oldState.unPress(storage.listener, likesDislikes)
                    storage.likes = likesDislikes.first
                    storage.dislikes = likesDislikes.second
                }
                // если сейчас что-то нажато
                else -> {
                    // если раньше было что-то нажато (нужно отжать старое)
                    if (oldState != None){
                        likesDislikes = oldState.unPress(storage.listener, likesDislikes)
                        storage.likes = likesDislikes.first
                        storage.dislikes = likesDislikes.second
                    }
                    // нажать на новое
                    likesDislikes = storage.feedBackState.pressOnMe(storage.listener, likesDislikes)
                    storage.likes = likesDislikes.first
                    storage.dislikes = likesDislikes.second
                }
            }
        }
    }
}