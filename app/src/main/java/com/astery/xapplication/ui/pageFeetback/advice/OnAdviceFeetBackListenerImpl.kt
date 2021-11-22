package com.astery.xapplication.ui.pageFeetback.advice

import com.astery.xapplication.model.entities.FeedBackState
import com.astery.xapplication.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class OnAdviceFeetBackListenerImpl(private val viewModelScope:CoroutineScope, val repository: Repository): OnAdviceFeetbackListener {
    override fun onLike(id: Int) {
        viewModelScope.launch {
            repository.likeAdvice(id)
        }
    }

    override fun onDislike(id: Int) {
        viewModelScope.launch {
            repository.dislikeAdvice(id)
        }
    }

    override fun onCancelLike(id: Int) {
        viewModelScope.launch {
            repository.cancelLikeAdvice(id)
        }
    }

    override fun onCancelDisLike(id: Int) {
        viewModelScope.launch {
            repository.cancelDislikeAdvice(id)
        }
    }

    override fun onChangeFeetbackState(id: Int, feedBackState: FeedBackState) {
        viewModelScope.launch {
            repository.changeFeetBackStateForAdvice(id, feedBackState)
        }
    }
}

