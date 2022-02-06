package com.astery.xapplication.ui.adviceUtils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.astery.xapplication.databinding.UnitAdviceBinding
import com.astery.xapplication.model.entities.Advice
import com.astery.xapplication.ui.pageFeetback.advice.AdviceFeedbackDelegate
import com.astery.xapplication.ui.pageFeetback.advice.OnAdviceFeedbackListener

class AdviceRenderer {
    fun render(i: Advice, feedbackListener:OnAdviceFeedbackListener?, context: Context): View {
        val adviceBinding = UnitAdviceBinding.inflate(LayoutInflater.from(context))
        adviceBinding.advice = i
        adviceBinding.feedBack =
            AdviceFeedbackDelegate(i, adviceBinding, feedbackListener).getValue()
        return adviceBinding.root
    }
}