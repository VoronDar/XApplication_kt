package com.astery.xapplication.ui.adviceUtils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.astery.xapplication.databinding.UnitAdviceBinding
import com.astery.xapplication.model.entities.Advice
import com.astery.xapplication.ui.pageFeetback.advice.AdviceFeetBackDelegate
import com.astery.xapplication.ui.pageFeetback.advice.OnAdviceFeetbackListener

class AdviceRenderer {
    fun render(i: Advice, feedbackListener:OnAdviceFeetbackListener?, context: Context): View {
        val adviceBinding = UnitAdviceBinding.inflate(LayoutInflater.from(context))
        adviceBinding.advice = i
        adviceBinding.feedBack =
            AdviceFeetBackDelegate(i, adviceBinding, feedbackListener).getValue()
        return adviceBinding.root
    }
}