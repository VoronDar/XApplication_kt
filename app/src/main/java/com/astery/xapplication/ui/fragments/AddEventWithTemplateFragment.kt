package com.astery.xapplication.ui.fragments

import android.annotation.SuppressLint
import android.content.res.Resources.getSystem
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
import com.astery.xapplication.ui.adapters.BlockListener
import com.astery.xapplication.ui.adapters.EventTemplateAdapter
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.astery.xapplication.viewModels.AddEventWithTemplateViewModel
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

/**
 * menu -> calendar > add Event with category -> add event with template
 * */
@AndroidEntryPoint
class AddEventWithTemplateFragment : XFragment() {
    private val binding: FragmentItemsBinding
        get() = bind as FragmentItemsBinding

    private val viewModel:AddEventWithTemplateViewModel by viewModels()

    private var adapter: EventTemplateAdapter? = null

    private var selectedDay: Calendar? = null
    private var selectedCategory: EventCategory? = null

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun prepareAdapters(){
        adapter = EventTemplateAdapter(requireContext())
        adapter!!.blockListener = (object: BlockListener {
            override fun onClick(position: Int) {
                moveNext(viewModel.templates.value!![position])
            }
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }

    override fun setViewModelListeners() {
        viewModel.loadTemplates(selectedCategory!!)
        viewModel.templates.observe(viewLifecycleOwner){
            Timber.i("got templates")
            adapter!!.units = it
        }
    }

    override fun getTitle(): String {
        return requireContext().resources.getString(R.string.title_new_event)
    }

    /** move to AddEvent */
    private fun moveNext(template:EventTemplate){
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(AddEventWithTemplateFragmentDirections.
        actionAddEventWithTemplateFragmentToAddEventFragment2(selectedDay!!, template))
    }


}
val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()
val Int.dp: Int get() = (this / getSystem().displayMetrics.density).toInt()