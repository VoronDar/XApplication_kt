package com.astery.xapplication.ui.fragments.addEvent.customizeEvent

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.R
import com.astery.xapplication.databinding.FragmentAddEventBinding
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.article.Presentable
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * menu -> calendar > add Event with category -> add event with template -> add event
 *
 * when this page is opened the first time with the current template, questions, answers and tips are loaded
 * item loaded later
 *
 * */
@AndroidEntryPoint
class AddEventFragment : XFragment() {
    private val binding: FragmentAddEventBinding
        get() = bind as FragmentAddEventBinding

    private val viewModel: AddEventViewModel by viewModels()
    private var questionsAdapter: QuestionsAdapter? = null

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
        viewModel.loadQuestions()
        viewModel.questions.observe(viewLifecycleOwner){
            questionsAdapter?.units = it
        }
        viewModel.addEventState.observe(viewLifecycleOwner){
            if (it == AddEventViewModel.JobState.Success){
                moveNext()
            }
        }
    }

    override fun prepareAdapters() {
        questionsAdapter = QuestionsAdapter(null, requireContext())
        binding.recyclerView.adapter = questionsAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        renderImage(viewModel.template!!.image)

    }

    private fun renderImage(bitmap: Bitmap?){
        binding.eventImage.isGone = bitmap == null
        if (bitmap != null) binding.eventImage.setImageBitmap(bitmap)
    }

    override fun getFragmentTitle(): String {
        return requireContext().resources.getString(R.string.title_new_event)
    }


    private fun moveNext(){
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        if (viewModel.event?.isAdvices == true)
            move(AddEventFragmentDirections.actionAddEventFragment2ToEndEventFragment(viewModel.event!!))
        else
            move(AddEventFragmentDirections.actionAddEventFragment2ToCalendarFragment())
    }
}