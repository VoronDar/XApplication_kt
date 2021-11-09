package com.astery.xapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.astery.xapplication.databinding.FragmentCalendarBinding


class CalendarFragment : XFragment() {
    private val binding: FragmentCalendarBinding
        get() = bind as FragmentCalendarBinding

    //private val viewModel: CalendarViewModel by viewModels()

    //private lateinit var cAdapter: CalendarAdapter
    //private lateinit var eAdapter: EventAdapter

    //private lateinit var now:Calendar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentCalendarBinding.inflate(inflater, container, false)
        return bind.root
    }


    override fun setListeners(){
        //binding.backIcon.setOnClickListener { changeEventPresent(false) }

        binding.noCardInfo.setOnClickListener {

        }
    }

    override fun setViewModelListeners() {
        TODO("Not yet implemented")
    }

    override fun prepareAdapters() {
        TODO("Not yet implemented")
    }

    override fun getTitle(): String? {
        TODO("Not yet implemented")
    }
/*
    override fun prepareAdapters(){
        // days
        val units = getDayUnitList()
        cAdapter = CalendarAdapter(units, context)
        cAdapter.notifyDataSetChanged()
        cAdapter.setBlockListener { position ->
            viewModel.changeDay(cAdapter.units[position].day)
        }


        binding.recyclerView.adapter = cAdapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 7, RecyclerView.HORIZONTAL, false)


        // events for one day
        eAdapter = EventAdapter(null, context)
        eAdapter.notifyDataSetChanged()
        eAdapter.setBlockListener { position ->
            if (position == 0)
                getPreparedToMoveListener(FragmentNavController.ADD_EVENT, addEventBundle.getBundle()).done(true)
            else {
                viewModel.selectedEvent.value = position
                changeEventPresent(true)
            }
        }

        binding.eventRecycler.adapter = eAdapter
        binding.eventRecycler.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)


    }

    @SuppressLint("Recycle")
    override fun setViewModelListeners() {
        /*
        viewModel.repository = (requireActivity().application as App).container.repository

        viewModel.selectedDay.observe(viewLifecycleOwner, {
            now = it

            binding.date.text = getString(R.string.calendar_date, now.get(Calendar.DAY_OF_MONTH),
                viewModel.getMonth(now.get(Calendar.MONTH), context), now.get(Calendar.YEAR))
            cAdapter.setSelectedDay(now.get(Calendar.DAY_OF_MONTH))
            viewModel.updateEvents()

            super.setTitle()

        })

        viewModel.events.observe(viewLifecycleOwner,{

            val noEvents = it.size == 1

            // go from anywhere to page without events
            if (binding.noCardInfo.visibility != VS.get(noEvents)){
                val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, (noEvents))
                TransitionManager.beginDelayedTransition(binding.fragmentRoot, sharedAxis)
                binding.eventRecycler.visibility = VS.get(!noEvents)
                binding.eventContainer.visibility = VS.get(false)
                binding.noCardInfo.visibility = VS.get(noEvents)
            }
            // go from eventInfo to page with events
            else if (binding.eventContainer.visibility == VISIBLE){

                val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, (false))
                TransitionManager.beginDelayedTransition(binding.fragmentRoot, sharedAxis)
                binding.eventRecycler.visibility = VS.get(true)
                binding.eventContainer.visibility = VS.get(false)
            }
            // go from page without event to page without events (blink)
            else if (binding.noCardInfo.visibility == VISIBLE && noEvents){
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

            eAdapter.setUnits(it as java.util.ArrayList<Event>?)



         */
        })

    }

    /** get units for calendar */
    private fun getDayUnitList(): ArrayList<DayUnit>{
        val units = ArrayList<DayUnit>()
        val cal:Calendar = viewModel.selectedDay.value!!
        for (i in 1..cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            units.add(DayUnit(i, true))
        }

        val firstDay:Calendar=GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1)
        for (i in Calendar.SUNDAY until firstDay.get(Calendar.DAY_OF_WEEK)){
            units.add(0, DayUnit(i, false))
        }

        return units
    }

    /** open or close event */
    private fun changeEventPresent(isOpen: Boolean){

        if (isOpen){
            viewModel.getTemplate { success ->
                if (success) {
                    val event =viewModel.events.value?.get(viewModel.selectedEvent.value!!)!!

                    val bundle = Bundle()
                    bundle.putSerializable("event", event)
                    clickToMove(binding.eventContent.getATip, FragmentNavController.GET_A_TIP, bundle)

                    binding.eventContent.eventName.text = event.template.name
                    binding.eventContent.eventDescription.text = event.template.description

                    var properties = ""
                    if (event.eventDescription != null)
                        for (i in event.template.questions){
                            properties += i.selectedAnswer.text + "\n\n"
                        }

                    binding.eventContent.eventProperties.text = properties
                    binding.eventContent.getATip.visibility = VS.get(event.isTips)

                    showEventContainer(true)
                }
            }


        } else showEventContainer(false)
    }

    /** swap event list and event info */
    private fun showEventContainer(show:Boolean){
        val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, show)
        TransitionManager.beginDelayedTransition(binding.fragmentRoot, sharedAxis)


        binding.eventRecycler.visibility = VS.get(!show)
        binding.eventContainer.visibility = VS.get(show)
        binding.backIcon.visibility = VS.get(show)
        }

    override fun getTitle(): String {
        if (this::now.isInitialized)
            return getString(R.string.title_calendar,
                viewModel.getMonth(now.get(Calendar.MONTH), context), now.get(Calendar.YEAR))
        return ""
    }

    override fun requireSearch(): Boolean {
        return false
    }

    override fun onBackPressed(): Boolean {
        if (binding.eventContainer.visibility == VISIBLE){
            showEventContainer(false)
            return true
        }
        return false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        changeMonthListener.close = true
        (activity as ParentActivity).showMenuNav(false, changeMonthListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeMonthListener.close = false
        (activity as ParentActivity).showMenuNav(true, changeMonthListener)
    }

    private val changeMonthListener:MenuNavListener = object: MenuNavListener() {
        override fun click(back: Boolean) {
            viewModel.changeMonth(!back)
            cAdapter.units = getDayUnitList()
            cAdapter.setSelectedDay(1)
        }
    }


    /** return bundle for creating a new event */
    private val addEventBundle: BundleGettable = object: BundleGettable {
        override fun getBundle(): Bundle {
            val bundle = Bundle()
            bundle.putSerializable("date", now)
            return bundle
        }
    }



    abstract class MenuNavListener{
        var close = false
        abstract fun click(back:Boolean)
    }

 */
}
