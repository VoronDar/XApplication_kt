package com.astery.xapplication.ui.fragments.calendar

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.R
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.ui.fragments.calendar.calendar_adapter.DayUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CalendarViewModel @Inject constructor(): ViewModel() {
    @set:Inject lateinit var repository: Repository

    private val _selectedDay: MutableLiveData<Calendar> = MutableLiveData<Calendar>()
    val selectedDay:LiveData<Calendar>
        get() = _selectedDay


    private val _events: MutableLiveData<List<Event?>>
    val events:LiveData<List<Event?>>
        get() = _events

    private val _selectedEvent: MutableLiveData<Pair<Event, Int>>
    val selectedEvent:LiveData<Pair<Event, Int>>
        get() = _selectedEvent



    init {
        _selectedDay.value = GregorianCalendar.getInstance()
        _events = MutableLiveData<List<Event?>>()
        _selectedEvent = MutableLiveData<Pair<Event, Int>>()

        
        Timber.i("calendar value ${selectedDay.value}")
    }

    fun setSelectedEvent(position:Int){
        viewModelScope.launch {
            val event = events.value!![position]!!
            repository.getDescriptionForEvent(event)
            _selectedEvent.value = Pair(events.value!![position]!!, position)
        }
    }

    /** change current date if the user changes month  */
    fun changeMonth(isMore: Boolean) {
        var month: Int = _selectedDay.value!!.get(Calendar.MONTH)
        var year: Int = _selectedDay.value!!.get(Calendar.YEAR)
        if (isMore) {
            month += 1
            if (month < Calendar.JANUARY) {
                month = Calendar.DECEMBER
                year -= 1
            }
        } else {
            month -= 1
            if (month > Calendar.DECEMBER) {
                month = Calendar.JANUARY
                year += 1
            }
        }
        _selectedDay.value = GregorianCalendar(year, month, 1)
        changeDay(1)
    }

    /** change current date if the user changes day  */
    fun changeDay(day: Int) {
        _selectedDay.value = GregorianCalendar(
            _selectedDay.value!!.get(Calendar.YEAR),
            _selectedDay.value!!.get(Calendar.MONTH), day
        )
    }

    /** get events for this day  */
    fun updateEvents() {

        viewModelScope.launch {
            _events.value = addFirstItem(repository.getEventsByDay(selectedDay.value!!))
        }
    }

    /** add empty unit in events list (it used for adding more events)  */
    private fun addFirstItem(units: List<Event?>): List<Event?>{
        val list:ArrayList<Event?> = ArrayList(units)
        list.add(0, null)
        return list
    }

    /** convert Calendar.get(MONTH) to String  */
    fun getMonth(calendar: Int, context: Context): String {
        return when (calendar) {
            Calendar.JANUARY -> context.resources.getString(R.string.calendar_january)
            Calendar.FEBRUARY -> context.resources.getString(R.string.calendar_february)
            Calendar.MARCH -> context.resources.getString(R.string.calendar_march)
            Calendar.APRIL -> context.resources.getString(R.string.calendar_april)
            Calendar.MAY -> context.resources.getString(R.string.calendar_may)
            Calendar.JUNE -> context.resources.getString(R.string.calendar_june)
            Calendar.JULY -> context.resources.getString(R.string.calendar_july)
            Calendar.AUGUST -> context.resources.getString(R.string.calendar_august)
            Calendar.SEPTEMBER -> context.resources.getString(R.string.calendar_september)
            Calendar.OCTOBER -> context.resources.getString(R.string.calendar_october)
            Calendar.NOVEMBER -> context.resources.getString(R.string.calendar_november)
            Calendar.DECEMBER -> context.resources.getString(R.string.calendar_december)
            else -> throw RuntimeException("CalendarViewModel.getMonth got invalid calendar $calendar")
        }
    }


    /** get units for calendar */
    fun getDayUnitList(): ArrayList<DayUnit> {
        val units = ArrayList<DayUnit>()
        val cal:Calendar = selectedDay.value!!
        for (i in 1..cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            units.add(DayUnit(i, true))
        }

        val firstDay:Calendar=GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1)
        for (i in Calendar.SUNDAY until firstDay.get(Calendar.DAY_OF_WEEK)){
            units.add(0, DayUnit(i, false))
        }

        return units
    }
}