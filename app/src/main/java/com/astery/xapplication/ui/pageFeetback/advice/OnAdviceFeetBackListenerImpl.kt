package com.astery.xapplication.ui.pageFeetback.advice

import com.astery.xapplication.model.entities.FeedBackState
import com.astery.xapplication.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber


class OnAdviceFeetBackListenerImpl(private val viewModelScope:CoroutineScope, val repository: Repository): OnAdviceFeetbackListener {
    override fun onLike(id: Int, nowLikes:Int, nowDislikes:Int) {
        viewModelScope.launch {
            repository.likeAdvice(id, nowLikes, nowDislikes)
        }
    }

    override fun onDislike(id: Int, nowLikes:Int, nowDislikes:Int) {
        viewModelScope.launch {
            repository.dislikeAdvice(id, nowLikes, nowDislikes)
        }
    }

    override fun onCancelLike(id: Int, nowLikes:Int, nowDislikes:Int) {
        viewModelScope.launch {
            repository.cancelLikeAdvice(id, nowLikes, nowDislikes)
        }
    }

    override fun onCancelDisLike(id: Int, nowLikes:Int, nowDislikes:Int) {
        viewModelScope.launch {
            repository.cancelDislikeAdvice(id, nowLikes, nowDislikes)
        }
    }
}

