package com.astery.xapplication.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.R
import com.astery.xapplication.databinding.FragmentCategoryBinding
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.ui.adapters.BlockListener
import com.astery.xapplication.ui.adapters.CategoryAdapter
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * menu -> calendar > add Event
 * */
@AndroidEntryPoint
class AddEventWithCategoryFragment : XFragment() {
    private val binding: FragmentCategoryBinding
        get() = bind as FragmentCategoryBinding

    private var adapter: CategoryAdapter? = null

    private var selectedDay: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))

        arguments?.let {
            selectedDay = it.getSerializable("day") as Calendar
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentCategoryBinding.inflate(inflater, container, false)
        return bind.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun prepareAdapters(){

        adapter = CategoryAdapter(EventCategory.toArray(), requireContext())
        adapter!!.blockListener = (object: BlockListener {
            override fun onClick(position: Int) {
                moveNext(position)
            }
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)


    }

    override fun getTitle(): String {
        return requireContext().resources.getString(R.string.title_new_event)
    }

    /** move to AddEventWithTemplate */
    private fun moveNext(pos:Int){
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(AddEventWithCategoryFragmentDirections.
        actionAddEventFragmentToAddEventWithTemplateFragment(selectedDay!!, pos))
    }


}
