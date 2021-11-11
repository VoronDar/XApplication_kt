package com.astery.xapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.astery.xapplication.R
import com.astery.xapplication.databinding.FragmentAddEventBinding
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.astery.xapplication.viewModels.AddEventViewModel
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * menu -> calendar > add Event with category -> add event with template -> add event
 * */
@AndroidEntryPoint
class AddEventFragment : XFragment() {
    private val binding: FragmentAddEventBinding
        get() = bind as FragmentAddEventBinding

    private val viewModel:AddEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))

        arguments?.let {
            viewModel.selectedDay = it.getSerializable("day") as Calendar
            viewModel.template = it.getSerializable("template") as EventTemplate
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentAddEventBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setListeners() {
        binding.submit.setOnClickListener{ viewModel.addEvent() }
    }

    override fun setViewModelListeners() {
        viewModel.addEventState.observe(viewLifecycleOwner){
            if (it == AddEventViewModel.JobState.Success){
                moveNext()
            }
        }
    }

    override fun getTitle(): String {
        return requireContext().resources.getString(R.string.title_new_event)
    }

    /** move to calendar
     * TODO(after adding questions - move to CongratulationsEvent)
     * */
    private fun moveNext(){
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(AddEventFragmentDirections.
            actionAddEventFragment2ToCalendarFragment())
    }


}
