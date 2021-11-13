package com.astery.xapplication.ui.fragments.calendar

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.astery.xapplication.R
import com.astery.xapplication.databinding.FragmentCalendarBinding
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.ui.adapterUtils.BlockListener
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.calendar.calendar_adapter.CalendarAdapter
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CalendarFragment : XFragment() {
    private val binding: FragmentCalendarBinding
        get() = bind as FragmentCalendarBinding

    private val viewModel: CalendarViewModel by viewModels()

    // TODO(change Calendar from recyclerview to just grid view)
    private var cAdapter: CalendarAdapter? = null
    private var eAdapter: EventAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentCalendarBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return bind.root
    }


    override fun setListeners(){
        binding.backIcon.setOnClickListener { showEventContainer(false) }
        binding.noCardInfo.setOnClickListener {moveToAddNewEvent()}
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun prepareAdapters(){
        // calendar
        cAdapter = CalendarAdapter(viewModel.getDayUnitList(), requireContext())
        cAdapter!!.blockListener = (object: BlockListener {
            override fun onClick(position: Int) {
                viewModel.changeDay(cAdapter!!.units!![position].day)
            }
        })

        binding.recyclerView.adapter = cAdapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 7, RecyclerView.HORIZONTAL, false)


        // events for one day
        eAdapter = EventAdapter(null, requireContext())
        eAdapter!!.blockListener = (object:BlockListener{
            override fun onClick(position: Int) {
                if (position == 0) {
                    moveToAddNewEvent()
                }else {
                    viewModel.setSelectedEvent(position)
                }
            }
        })

        binding.eventRecycler.adapter = eAdapter
        binding.eventRecycler.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
    }

    @SuppressLint("Recycle")
    override fun setViewModelListeners() {

        viewModel.selectedDay.observe(viewLifecycleOwner, {
            cAdapter?.setSelectedDay(it.get(Calendar.DAY_OF_MONTH))
            viewModel.updateEvents()
            super.setTitle()

        })

        viewModel.events.observe(viewLifecycleOwner,{
            val noEvents = (it.size == 1)

            // go from anywhere to page without events
            if (binding.noCardInfo.isVisible != noEvents){
                val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, (noEvents))
                TransitionManager.beginDelayedTransition(binding.fragmentRoot, sharedAxis)
                binding.eventRecycler.isGone = noEvents
                binding.eventContainer.isGone = true
                binding.noCardInfo.isGone = !noEvents
            }
            // go from eventInfo to page with events
            else if (binding.eventContainer.isVisible){

                val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, (false))
                TransitionManager.beginDelayedTransition(binding.fragmentRoot, sharedAxis)
                binding.eventRecycler.isGone = false
                binding.eventContainer.isGone = true
            }
            // go from page without event to page without events (blink)
            else if (binding.noCardInfo.isVisible && noEvents){
                val alphaAnimator = ValueAnimator.ofFloat(1f, 0.75f)
                alphaAnimator.addUpdateListener { animator ->
                    val value = animator.animatedValue as Float
                    binding.noCardInfo.alpha = value
                }
                alphaAnimator.repeatMode = REVERSE
                alphaAnimator.repeatCount = 1

                val transitionAnimator = ValueAnimator.ofFloat(0f, 25f)
                transitionAnimator.addUpdateListener { animator ->
                    val value = animator.animatedValue as Float
                    binding.noCardInfo.translationY = value
                }
                transitionAnimator.repeatMode = REVERSE
                transitionAnimator.repeatCount = 1

                val valueAnimator = AnimatorSet()
                valueAnimator.play(alphaAnimator).with(transitionAnimator)
                valueAnimator.interpolator = AccelerateDecelerateInterpolator()
                valueAnimator.duration = 150
                valueAnimator.start()
            }

            eAdapter?.units = (it as ArrayList<Event?>)

        })

        viewModel.selectedEvent.observe(viewLifecycleOwner){
            binding.eventContent.getATip.setOnClickListener{
                moveToActionForTip()
            }
            showEventContainer(true)
            viewModel.selectedEvent.value!!.first.isAdvices
        }
    }

    /** swap event list and event info */
    private fun showEventContainer(show:Boolean){
        val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, show)
        TransitionManager.beginDelayedTransition(binding.fragmentRoot, sharedAxis)

        binding.eventRecycler.isGone = show
        binding.eventContainer.isGone = !show
        binding.backIcon.isGone = !show
        }

    override fun getFragmentTitle(): String? {
        val now = viewModel.selectedDay.value
        if (now != null) {
            return getString(
                R.string.title_calendar,
                viewModel.getMonth(now.get(Calendar.MONTH), requireContext()), now.get(Calendar.YEAR)
            )
        }
        return null
    }

    /** get actions with */
    private fun moveToActionForTip(){
        TODO()
    }
    private fun moveToAddNewEvent() {
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(CalendarFragmentDirections.actionCalendarFragmentToAddEventFragment(
                viewModel.selectedDay.value!!
            )
        )
    }


    object BindingAdapters{
        @BindingAdapter("app:properties")
        @JvmStatic fun setEventProperties(view: TextView?, event: Event?) {
            // TODO (add questions later)
            val properties = StringBuilder()
            /*
            if (event?.eventDescription != null) {
            for (i in event.template?.questions) {
                properties.append(i.selectedAnswer.text).append("\n\n")
            }
             */
            view?.text = properties.toString()
        }

    }


}
