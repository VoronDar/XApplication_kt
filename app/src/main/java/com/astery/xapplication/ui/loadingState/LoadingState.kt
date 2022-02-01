package com.astery.xapplication.ui.loadingState

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.astery.xapplication.R
import com.astery.xapplication.databinding.LoadingStateIconBinding

/** base class for all loading states. Implements template pattern\n
 * the meaning of each loadingState you can see in the figma design: components
 * */
sealed class LoadingStateTemplate {

    /** should be called when fragment/activity calls onPause() */
    open fun doOnPauseUI() {}

    /** should be called when fragment/activity calls onResume() */
    open fun doOnResumeUI() {}

    open fun doOnDestroyUI(){ removeView() }



    abstract fun getIconDrawable(context:Context): Drawable
    abstract fun getTitle(res: Resources): String

    /** returns true if this state has the 'reload' button*/
    open fun isReloadable(): Boolean = false

    /** calls listener when the 'reload' button is pressed (if it presents)*/
    var reloadListener: () -> Unit = {}
    protected set

    protected open fun onGetIconView(view: View) {}

    companion object{
        private var lastContainer:ViewGroup? = null
        private var lastBinding:LoadingStateIconBinding? = null
        fun addRootToViewGroup(me:LoadingStateTemplate, inflater: LayoutInflater, container: ViewGroup) {
            // if
            if (container != lastContainer) {
                val binding = LoadingStateIconBinding.inflate(inflater, container, false)
                binding.loadingState = me
                me.onGetIconView(binding.icon)
                container.addView(binding.root)
                lastBinding = binding
            } else{
                me.onGetIconView(lastBinding!!.icon)
                lastBinding?.loadingState = me
            }
            lastContainer = container
            me.doOnResumeUI()

        }

        fun removeView(){
            lastContainer?.removeView(lastBinding?.root)
            lastBinding?.unbind()
            lastContainer = null
            lastBinding = null
        }

    }


}

class LoadingStateLoading : LoadingStateTemplate() {

    /** animation for infinite full rotation */
    private val rotationAnimator: ValueAnimator by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.duration = 700
        animator.interpolator = LinearInterpolator()
        animator
    }

    override fun getIconDrawable(context: Context): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.ic_loading_state_loading)!!
    }

    override fun getTitle(res: Resources): String {
        return res.getString(R.string.loading_state_loading)
    }

    /** stop the rotate animation */
    override fun doOnPauseUI() {
        rotationAnimator.cancel()
    }

    /** resume the rotate animation */
    override fun doOnResumeUI() {
        rotationAnimator.start()
    }

    override fun onGetIconView(view: View) {
        rotationAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            view.rotation = value
        }
    }
}


class LoadingStateNothing : LoadingStateTemplate() {
    override fun getIconDrawable(context:Context): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.ic_loading_state_nothing)!!
    }

    override fun getTitle(res: Resources): String {
        return res.getString(R.string.loading_state_nothing)
    }
}


class LoadingStateError(private val reason: LoadingErrorReason, reloadListener: () -> Unit) :
    LoadingStateTemplate() {
    init {
        this.reloadListener = reloadListener
    }

    override fun getIconDrawable(context:Context): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.ic_loading_state_error)!!
    }

    /** besides the title of loading state returns description */
    override fun getTitle(res: Resources): String {
        return res.getString(R.string.loading_state_error, reason.getStringForUI(res))
    }

    override fun isReloadable() = true
}

