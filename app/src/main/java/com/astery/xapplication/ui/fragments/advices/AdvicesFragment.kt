package com.astery.xapplication.ui.fragments.advices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.R
import com.astery.xapplication.databinding.FragmentAdvicesBinding
import com.astery.xapplication.model.entities.Item
import com.astery.xapplication.model.entities.Question
import com.astery.xapplication.ui.adapterUtils.BlockListener
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.astery.xapplication.ui.loadingState.LoadingStateError
import com.astery.xapplication.ui.loadingState.LoadingStateLoading
import com.astery.xapplication.ui.loadingState.LoadingStateView
import com.astery.xapplication.ui.loadingState.UnexpectedBugException
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

/**
 * menu - calendarFragment - advicesFragment
 * */
@AndroidEntryPoint
class AdvicesFragment : XFragment() {
    private val binding: FragmentAdvicesBinding
        get() = bind as FragmentAdvicesBinding

    private val viewModel: AdvicesViewModel by viewModels()
    private var adapter: AdvicesAdapter? = null
    private var loadingState: LoadingStateView? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))

        arguments?.let {
            viewModel.setQuestions(it.getParcelableArray("questions")?.toList() as? List<Question>)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentAdvicesBinding.inflate(inflater, container, false)
        return bind.root
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


    override fun prepareAdapters() {
        adapter = AdvicesAdapter(null, requireContext(), null)
        adapter!!.blockListener = (object : BlockListener {
            override fun onClick(position: Int) {
                moveToActionForItem(viewModel.getItemForQuestion(position))
            }
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    override fun setViewModelListeners() {
        askForAdvices()
        viewModel.units.observe(viewLifecycleOwner) { units ->
            adapter?.units = units
            adapter?.feedbackListener = viewModel.feedbackListener

            if (units.isNotEmpty()) LoadingStateView.removeView()
            else loadingState = LoadingStateView.addViewToViewGroup(
                LoadingStateError(
                    UnexpectedBugException(),
                    ::askForAdvices
                ), layoutInflater, binding.frame
            )
        }
    }

    private fun askForAdvices() {
        loadingState = LoadingStateView.addViewToViewGroup(
            LoadingStateLoading(),
            layoutInflater,
            binding.frame
        )
        viewModel.loadAdvices()

    }

    override fun getFragmentTitle(): String = getString(R.string.title_tips)

    /** get action for item */
    private fun moveToActionForItem(item: Item) {
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(
            AdvicesFragmentDirections.actionAdvicesFragmentToItemFragment(item)
        )
    }

}
