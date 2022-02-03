package com.astery.xapplication.ui.fragments.addEvent.selectTemplate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.R
import com.astery.xapplication.databinding.FragmentItemsBinding
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.ui.adapterUtils.BlockListener
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.astery.xapplication.ui.loadingState.*
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * menu -> calendar > add Event with category -> add event with template
 * */
@AndroidEntryPoint
class AddEventWithTemplateFragment : XFragment() {
    private val binding: FragmentItemsBinding
        get() = bind as FragmentItemsBinding

    private val viewModel: AddEventWithTemplateViewModel by viewModels()

    private var adapter: EventTemplateAdapter? = null

    private var selectedDay: Calendar? = null
    private var selectedCategory: EventCategory? = null
    private var loadingState: LoadingStateView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))

        arguments?.let {
            selectedDay = it.getSerializable("day") as Calendar
            selectedCategory = EventCategory.values()[it.getInt("category")]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentItemsBinding.inflate(inflater, container, false)
        return bind.root
    }

    private var isCreated = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isCreated) {
            prepareAdapters()
        } else super.onViewCreated(view, savedInstanceState)
        isCreated
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun prepareAdapters() {
        adapter = EventTemplateAdapter(requireContext())
        adapter!!.blockListener = (object : BlockListener {
            override fun onClick(position: Int) {
                moveNext(viewModel.templates.value!!.getOrThrow()[position])
            }
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }


    override fun onPause() {
        super.onPause()
        loadingState?.doOnPauseUI()
    }

    override fun onResume() {
        super.onResume()
        loadingState?.doOnResumeUI()
    }

    override fun onStop() {
        super.onStop()
        loadingState?.doOnStopUI()
    }

    override fun setViewModelListeners() {
        loadingState = LoadingStateView.addViewToViewGroup(
            LoadingStateLoading(),
            layoutInflater,
            binding.frame
        )


        viewModel.loadTemplates(selectedCategory!!)
        viewModel.templates.observe(viewLifecycleOwner) {
            if (it.isFailure) {
                loadingState =
                    LoadingStateView.addViewToViewGroup(LoadingStateError(it.exceptionOrNull()!! as LoadingErrorReason) {
                        loadingState = LoadingStateView.addViewToViewGroup(
                            LoadingStateLoading(),
                            layoutInflater,
                            binding.frame
                        )
                        viewModel.loadTemplates(selectedCategory!!)
                    }, layoutInflater, binding.frame)
            } else {
                val list = it.getOrThrow()
                if (list.isEmpty()) {
                    loadingState = LoadingStateView.addViewToViewGroup(
                        LoadingStateNothing(),
                        layoutInflater,
                        binding.frame
                    )
                } else {
                    LoadingStateView.removeView()
                    adapter!!.units = list
                    viewModel.loadImages(adapter!!)
                }
            }

        }
    }

    override fun getFragmentTitle(): String {
        return requireContext().resources.getString(R.string.title_new_event)
    }

    /** move to AddEvent */
    private fun moveNext(template: EventTemplate) {
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(
            AddEventWithTemplateFragmentDirections
                .actionAddEventWithTemplateFragmentToAddEventFragment2(selectedDay!!, template)
        )
    }


}