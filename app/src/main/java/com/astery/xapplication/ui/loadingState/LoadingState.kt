package com.astery.xapplication.ui.loadingState

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnCancel
import androidx.core.content.ContextCompat
import com.astery.xapplication.R
import com.astery.xapplication.databinding.LoadingStateIconBinding
import timber.log.Timber

/** base class for all loading states. Implements template pattern\n
 * the meaning of each loadingState you can see in the figma design: components: Loading State
 * */
sealed class LoadingStateView {

    /** should be called when fragment/activity calls onPause() */
    open fun doOnPauseUI() {}

    /** should be called when fragment/activity calls onResume() */
    open fun doOnResumeUI() {}

    /** should be called when fragment calls onStop() */
    open fun doOnStopUI(){ removeView() }



    abstract fun getIconDrawable(context:Context): Drawable
    abstract fun getTitle(res: Resources): String

    /** returns true if this state has the 'reload' button*/
    open fun isReloadable(): Boolean = false

    /** calls listener when the 'reload' button is pressed (if it presents)*/
    var reloadListener: () -> Unit = {}
    protected set

    protected open fun onGetIconView(view: View) {}

    companion object{
        private var lastState:LoadingStateView? = null
        private var lastContainer:ViewGroup? = null
        private var lastBinding:LoadingStateIconBinding? = null
        /**
         * add LoadingStateView to viewGroup (or just reload it, if it is already placed)
         *
         * @param me - new LoadingState
         * @param container - viewGroup that is used to place LoadingStateView
         *
         * @return me
         * */
        fun addViewToViewGroup(me:LoadingStateView, inflater: LayoutInflater, container: ViewGroup):LoadingStateView {
            // if
            lastState?.doOnPauseUI()
            if (container != lastContainer || lastBinding == null) {
                val binding = LoadingStateIconBinding.inflate(inflater, container, false)
                binding.loadingState = me
                me.onGetIconView(binding.icon)
                container.addView(binding.root)
                lastBinding = binding
            } else{
                me.onGetIconView(lastBinding!!.icon)
                lastBinding?.loadingState = me
            }
            lastState = me
            lastContainer = container
            me.doOnResumeUI()
            return me
        }

        /** remove LoadingStateView from the last container.
         * should be called when data is loaded.
         * You should not call it in onDestroy(), use doOnDestroy() instead
         * */
        fun removeView(){
            Timber.d("remove loading state view")
            lastContainer?.removeView(lastBinding?.root)
            lastBinding?.unbind()
            lastContainer = null
            lastBinding = null
            lastState = null
        }

    }


}
/**
 * says: loading...
 * */
class LoadingStateLoading : LoadingStateView() {

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
        rotationAnimator.doOnCancel {
            view.rotation = 0f
        }
    }
}


/** says: There is no data found (it is possible and it is not an error) */
class LoadingStateNothing : LoadingStateView() {
    override fun getIconDrawable(context:Context): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.ic_loading_state_nothing)!!
    }

    override fun getTitle(res: Resources): String {
        return res.getString(R.string.loading_state_nothing)
    }
}

/** says: An error occured + description */
class LoadingStateError(private val reason: LoadingErrorReason, reloadListener: () -> Unit) :
    LoadingStateView() {
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

