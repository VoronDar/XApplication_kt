package com.astery.xapplication.ui.fragments.calendar

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import com.astery.xapplication.ui.activity.interfaces.ParentActivity
import com.astery.xapplication.ui.adapterUtils.BlockListener
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.calendar.calendar_adapter.CalendarAdapter
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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


    override fun onResume() {
        super.onResume()
        (activity as ParentActivity).showMenuNav(true, changeMonthListener)
    }

    override fun onStop() {
        super.onStop()
        (activity as ParentActivity).showMenuNav(false, changeMonthListener)
    }

    override fun setListeners() {
        binding.backIcon.setOnClickListener { showEventContainer(false) }
        binding.noCardInfo.setOnClickListener { moveToAddNewEvent() }
        binding.deleteIcon.setOnClickListener { deleteEvent() }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun prepareAdapters() {
        // calendar
        if (cAdapter == null) {
            cAdapter = CalendarAdapter(viewModel.getDayUnitList(), requireContext())
        }
        cAdapter!!.blockListener = (object : BlockListener {
            override fun onClick(position: Int) {
                viewModel.changeDay(cAdapter!!.units!![position].day)
            }
        })

        binding.recyclerView.adapter = cAdapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 7, RecyclerView.HORIZONTAL, false)


        // events for one day
        if (eAdapter == null) {
            eAdapter = EventAdapter(null, requireContext())
        }
        eAdapter!!.blockListener = (object : BlockListener {
            override fun onClick(position: Int) {
                if (position == 0) {
                    moveToAddNewEvent()
                } else {
                    viewModel.setSelectedEvent(position)
                }
            }
        })

        binding.eventRecycler.adapter = eAdapter
        binding.eventRecycler.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
    }

    override fun setViewModelListeners() {
        prepareAdapters()
        viewModel.selectedDay.observe(viewLifecycleOwner, {
            viewModel.updateEvents(eAdapter!!)
            cAdapter?.selectedDay = (it.get(Calendar.DAY_OF_MONTH))
            super.setTitle()

        })

        viewModel.events.observe(viewLifecycleOwner, {
            val noEvents = (it.size == 1)

            // go from anywhere to page without events
            if (binding.noCardInfo.isVisible != noEvents) renderChangeNoEventsState(noEvents)
            // go from eventInfo to page with events
            else if (binding.eventContainer.isVisible) renderEvents()
            // go from page without event to page without events (blink)
            else if (binding.noCardInfo.isVisible && noEvents) renderNoEventsAgain()

            eAdapter?.units = (it as ArrayList<Event?>)

        })


        viewModel.selectedEvent.observe(viewLifecycleOwner) { eventPair ->
            binding.eventContent.getATip.setOnClickListener {
                moveToActionForTip()
            }
            showEventContainer(true)
            renderSelectedEventImage(eventPair.first.image)
            //eventPair.first.isAdvices
        }
    }

    /** blink noCard page */
    private fun renderNoEventsAgain() {

        // need to hide event parts if there is not events because the user just deleted them
        binding.eventContainer.isGone = true
        binding.eventRecycler.isGone = true

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

    /** animate from noCards to events */
    private fun renderEvents() {
        val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, (false))
        TransitionManager.beginDelayedTransition(binding.eventRoot, sharedAxis)
        binding.eventRecycler.isGone = false
        binding.eventContainer.isGone = true
    }

    /** animate noCards from events and vice versa */
    private fun renderChangeNoEventsState(noEvents: Boolean) {
        val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        TransitionManager.beginDelayedTransition(binding.eventRoot, sharedAxis)
        binding.eventRecycler.isGone = noEvents
        binding.eventContainer.isGone = true
        binding.noCardInfo.isGone = !noEvents
    }

    private var isShownEventContainer = false

    /** swap event list and event info */
    private fun showEventContainer(show: Boolean) {
        isShownEventContainer = show
        val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, show)
        TransitionManager.beginDelayedTransition(binding.fragmentRoot, sharedAxis)

        binding.eventRecycler.isGone = show
        binding.eventContainer.isGone = !show
        binding.backIcon.isGone = !show
        binding.deleteIcon.isGone = !show
    }

    private fun renderSelectedEventImage(image: Bitmap?) {
        if ((image != null))
            binding.eventContent.itemImage.setImageBitmap(image)
        else
            binding.eventContent.itemImage.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.dating
                )
            )
    }


    override fun getFragmentTitle(): String? {
        val now = viewModel.selectedDay.value
        if (now != null) {
            return getString(
                R.string.title_calendar,
                viewModel.getMonth(now.get(Calendar.MONTH), requireContext()),
                now.get(Calendar.YEAR)
            )
        }
        return null
    }

    /** get actions with */
    private fun moveToActionForTip() {
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(
            CalendarFragmentDirections.actionCalendarFragmentToAdvicesFragment(
                viewModel.selectedEvent.value!!.first.template!!.questions!!.toTypedArray()
            )
        )
    }

    private fun deleteEvent() {
        DeleteEventDialogue(layoutInflater, requireContext())
            .setOnOkListener {
                showEventContainer(false)
                viewModel.deleteEvent()
            }
            .show()
    }

    private val changeMonthListener: MenuNavListener = object : MenuNavListener() {
        override fun click(back: Boolean) {
            viewModel.changeMonth(!back)
            cAdapter?.units = viewModel.getDayUnitList()
            cAdapter?.selectedDay = 1
        }
    }


    /** move to fragment to add new event */
    private fun moveToAddNewEvent() {
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(
            CalendarFragmentDirections.actionCalendarFragmentToAddEventFragment(
                viewModel.selectedDay.value!!
            )
        )
    }


    abstract class MenuNavListener {
        var close = false
        abstract fun click(back: Boolean)
    }


    override fun onBackPressed(): Boolean {
        return if (isShownEventContainer) {
            binding.backIcon.performClick()
            true
        } else {
            false
        }
    }

    object BindingAdapters {
        @BindingAdapter("app:properties")
        @JvmStatic
        fun setEventProperties(view: TextView?, event: Event?) {
            //val list:ArrayList<Triple<Int, Int, AdviceType>> = arrayListOf()
            val properties = StringBuilder()
            if (event?.template?.questions != null) {
                for (i in event.template!!.questions!!) {
                    val selectedAnswer = i.selectedAnswer!!
                    //list.add(Triple(properties.length, selectedAnswer.body.length+2, selectedAnswer.item.))
                    properties.append(selectedAnswer.body).append("\n\n")
                }
                view?.text = properties.toString()
            } else
                view?.text = null

        }
    }


}
