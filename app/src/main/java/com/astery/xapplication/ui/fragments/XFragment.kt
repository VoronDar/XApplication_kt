package com.astery.xapplication.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.astery.xapplication.ui.activity.interfaces.ParentActivity
import com.astery.xapplication.ui.fragments.transitionHelpers.NavigationTransition
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.google.android.material.transition.MaterialSharedAxis

/**
 * base fragment for all
 * */
abstract class XFragment : Fragment() {


    protected var _bind: ViewDataBinding? = null
    protected val bind get() = _bind!!



    override fun onDestroyView() {
        super.onDestroyView()
        _bind?.unbind()
        _bind = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setViewModelListeners()
        setListeners()
        prepareAdapters()

    }

    protected fun move(action:NavDirections){
        findNavController().navigate(action)
    }

    /** set transition between two fragments */
    fun setTransition(transition: NavigationTransition){
        when(transition) {
            is SharedAxisTransition -> {
                if (transition.isFirst) {
                    exitTransition = MaterialSharedAxis(transition.axis, transition.firstParent)
                    reenterTransition = MaterialSharedAxis(transition.axis, !transition.firstParent)
                } else {
                    enterTransition = MaterialSharedAxis(transition.axis,  transition.firstParent)
                    returnTransition = MaterialSharedAxis(transition.axis, transition.firstParent)
                }
            }
        }
    }

    /** do smt when backPressed
     * @return false if there is no special action for back*/
    open fun onBackPressed():Boolean {return true}

    protected fun setTitle(){
        (activity as ParentActivity).changeTitle(getTitle())
    }


    /** set onClick listeners (mostly for applying actions)*/
    protected abstract fun setListeners()
    /** set listeners to viewModel changes */
    protected abstract fun setViewModelListeners()
    /** set units, layout params to adapters*/
    protected abstract fun prepareAdapters()

    /** return title */
    protected abstract fun getTitle():String?

}