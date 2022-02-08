package com.astery.xapplication.ui.activity.popupDialogue

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import com.astery.xapplication.R

/** open and close dialogue panel
 * disable and enable everything that needed
 * */
class DialoguePanel(val activity: PanelHolder) {
    companion object {
        var isOpened = false
        private set
    }

    private var binding: ViewDataBinding? = null
    private var specialBlockable: List<Blockable> = listOf()
    private var dialogueHolder: DialogueHolder? = null

    private val panel: FrameLayout by lazy(LazyThreadSafetyMode.NONE) { activity.findView(R.id.panel) as FrameLayout }
    private val backgroundPanel by lazy(LazyThreadSafetyMode.NONE) { activity.findView(R.id.background) }
    private val window by lazy(LazyThreadSafetyMode.NONE) { activity.findView(R.id.popup_window) }

    /**
     * activity(PanelHolder) provides views on the activity, that should be disable.
     * specialBlockable - views in the fragment, that should be disable
     * */
    fun openPanel(
        holder: DialogueHolder,
        specialBlockable: List<Blockable>
    ) {
        if (isOpened) {
            closePanel()
            return
        }
        isOpened = true

        this.dialogueHolder = holder
        this.specialBlockable = specialBlockable
        enableAllViews(false)

        window.isVisible = true

        this.binding = holder.getBinding(activity.getLayoutInflater(), panel, this::closePanel)
        panel.addView(this.binding!!.root)

        val panel = activity.findView(R.id.panel)
        panel.visibility = View.VISIBLE

        animatePanel(dp(400), 0f, 0f, 0.6f, ::onOpen)
    }

    fun closePanel() {
        if (!isOpened) {
            enableAllViews(true)
            return
        }

        dialogueHolder?.onEnablePanel(binding!!, false)
        animatePanel(0f, dp(800), 0.6f, 0f){
            panel.removeAllViews()
            onClose()
        }
    }

    private fun onClose() {
        window.isGone = true
        enableAllViews(true)
        isOpened = false
        binding?.unbind()
        dialogueHolder?.onClosePanel()
    }

    private fun onOpen() {
        dialogueHolder?.onEnablePanel(binding!!, true)
    }

    private fun animatePanel(
        fromTrans: Float,
        toTrans: Float,
        fromAlpha: Float,
        toAlpha: Float,
        doOnEnd: () -> Unit
    ) {
        val transitionAnimator = ValueAnimator.ofFloat(fromTrans, toTrans)
        transitionAnimator.addUpdateListener { animator ->
            val value = animator.animatedValue as Float
            panel.translationY = value
        }
        transitionAnimator.doOnEnd {
            doOnEnd()
        }
        val alphaAnimator = ValueAnimator.ofFloat(fromAlpha, toAlpha)
        alphaAnimator.addUpdateListener { animator ->
            val value = animator.animatedValue as Float
            backgroundPanel.alpha = value
        }
        val animator = AnimatorSet()
        animator.play(alphaAnimator).with(transitionAnimator)
        animator.interpolator = DecelerateInterpolator()
        animator.duration = 250
        animator.start()
    }


    private fun dp(dp: Int): Float {
        return dp * (activity.getContext().resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
    }

    private fun enableAllViews(enable: Boolean) {
        for (i in activity.getBlockable()) {
            i.setEnabled(enable)
        }
        for (i in specialBlockable) {
            i.setEnabled(enable)
        }
    }
}