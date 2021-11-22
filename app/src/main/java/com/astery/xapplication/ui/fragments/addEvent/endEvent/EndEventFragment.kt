package com.astery.xapplication.ui.fragments.addEvent.endEvent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.astery.xapplication.R
import com.astery.xapplication.databinding.FragmentDoneEventBinding
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

/**
 * menu -> calendar > add Event with category -> add event with template -> add event -> end event
 * */
@AndroidEntryPoint
class EndEventFragment : XFragment() {
    private val binding: FragmentDoneEventBinding
        get() = bind as FragmentDoneEventBinding


    private var event: Event? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))

        arguments?.let {
            event = it.getParcelable("event")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentDoneEventBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.fragment = this
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getFragmentTitle(): String {
        return requireContext().resources.getString(R.string.title_new_event)
    }


    fun moveToCalendar() {
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(EndEventFragmentDirections.actionEndEventFragmentToCalendarFragment())
    }

    fun moveToAdvices() {
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(EndEventFragmentDirections.actionEndEventFragmentToAdvicesFragment(event!!.template!!.questions!!.toTypedArray()))
    }

}
