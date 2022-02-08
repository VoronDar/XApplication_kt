package com.astery.xapplication.ui.pageFeetback.advice

import com.astery.xapplication.model.entities.FeedBackState
import com.astery.xapplication.model.entities.Question
import com.astery.xapplication.repository.FeedbackAction
import com.astery.xapplication.repository.FeedbackField
import com.astery.xapplication.repository.FeedbackResult
import com.astery.xapplication.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/** expansion of onFeedbackListener for advice (basic listener doesn't suit advices.
 * Because there are many advices for one view model)
 * It stores there because there are many fragments that have advices
 * */
class OnAdviceFeedbackListener(
    private val questions: List<Question>,
    private val viewModelScope: CoroutineScope,
    val repository: Repository
) {
    fun onLike(id: Int, nowLikes: Int, nowDislikes: Int) {
        viewModelScope.launch {
            if (repository.likeAdvice(id, nowLikes, nowDislikes))
                applyChangesToAdvice(
                    id, FeedbackResult(
                        FeedbackField.Like, FeedbackAction.Do,
                        nowLikes,
                        nowDislikes
                    )
                )
        }
    }

    fun onDislike(id: Int, nowLikes: Int, nowDislikes: Int) {
        viewModelScope.launch {
            if (repository.dislikeAdvice(id, nowLikes, nowDislikes))
                applyChangesToAdvice(
                    id, FeedbackResult(
                        FeedbackField.Dislike, FeedbackAction.Do,
                        nowLikes,
                        nowDislikes
                    )
                )
        }
    }

    fun onCancelLike(id: Int, nowLikes: Int, nowDislikes: Int) {
        viewModelScope.launch {
            if (repository.cancelLikeAdvice(id, nowLikes, nowDislikes))
                applyChangesToAdvice(
                    id, FeedbackResult(
                        FeedbackField.Like, FeedbackAction.Cancel,
                        nowLikes,
                        nowDislikes
                    )
                )
        }
    }

    fun onCancelDisLike(id: Int, nowLikes: Int, nowDislikes: Int) {
        viewModelScope.launch {
            if (repository.cancelDislikeAdvice(id, nowLikes, nowDislikes))
                applyChangesToAdvice(
                    id,
                    FeedbackResult(
                        FeedbackField.Dislike,
                        FeedbackAction.Cancel,
                        nowLikes,
                        nowDislikes
                    )
                )
        }
    }

    /** change feedback values of feedback and likes of advices in given list of questions*/
    private fun applyChangesToAdvice(id: Int, feedback: FeedbackResult) {
        question@ for (k in questions) {
            if (k.selectedAnswer?.item?.advices == null) continue
            for (j in k.selectedAnswer!!.item!!.advices!!) {
                if (j.id == id) {
                    j.feedback =
                        when {
                            feedback.action == FeedbackAction.Cancel -> FeedBackState.None
                            feedback.field == FeedbackField.Like -> FeedBackState.Like
                            else -> FeedBackState.Dislike
                        }
                    if (feedback.field == FeedbackField.Like) {
                        j.likes = when (feedback.action) {
                            FeedbackAction.Do -> feedback.nowLikes + 1
                            FeedbackAction.Cancel -> feedback.nowLikes - 1
                        }
                    } else {
                        j.dislikes = when (feedback.action) {
                            FeedbackAction.Do -> feedback.nowDislikes + 1
                            FeedbackAction.Cancel -> feedback.nowDislikes - 1
                        }
                    }
                    break@question
                }
            }
        }
    }
}

