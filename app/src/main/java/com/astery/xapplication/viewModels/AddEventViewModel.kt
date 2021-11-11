package com.astery.xapplication.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(): ViewModel() {
    @set:Inject lateinit var repository: Repository
    var template: EventTemplate? = null
    var selectedDay: Calendar? = null
    private val _addEventState:MutableLiveData<JobState> = MutableLiveData()
    val addEventState:LiveData<JobState>
        get() = _addEventState

    init{
        _addEventState.value = JobState.Idle
    }


    fun addEvent(){
        viewModelScope.launch {
            _addEventState.value = JobState.Running
            repository.addEvent(Event(null, template!!.id, null, selectedDay!!.time))
            _addEventState.value = JobState.Success
        }
    }

    enum class JobState{
        Idle,
        Running,
        Success
    }
}