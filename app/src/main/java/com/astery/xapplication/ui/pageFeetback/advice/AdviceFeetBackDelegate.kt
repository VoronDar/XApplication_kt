package com.astery.xapplication.ui.pageFeetback.advice

import com.astery.xapplication.databinding.UnitAdviceBinding
import com.astery.xapplication.model.entities.Advice
import com.astery.xapplication.model.entities.FeedBackState
import com.astery.xapplication.repository.feetback.OnFeedbackListener
import com.astery.xapplication.ui.pageFeetback.FeedBackStorage

class AdviceFeetBackDelegate(val i: Advice, val binding:UnitAdviceBinding, val feedbackListener: OnAdviceFeetbackListener?){
    fun getValue():FeedBackStorage{
        return FeedBackStorage(i.likes, i.dislikes, i.feedback, object: OnFeedbackListener {
            override fun onLike(){
                binding.feedBack = binding.feedBack
                feedbackListener?.onLike(i.id)
            }

            override fun onCancelLike() {
                binding.feedBack = binding.feedBack
                feedbackListener?.onCancelLike(i.id)
            }

            override fun onDislike(){
                binding.feedBack = binding.feedBack
                feedbackListener?.onDislike(i.id)
            }

            override fun onCancelDislike() {
                binding.feedBack = binding.feedBack
                feedbackListener?.onCancelLike(i.id)
            }

            override fun onChangeFeetBackState(feedBackState: FeedBackState) {
                binding.feedBack = binding.feedBack
                feedbackListener?.onCancelDisLike(i.id)
            }
        })
    }
}