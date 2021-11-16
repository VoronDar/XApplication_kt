package com.astery.xapplication.ui.fragments.addEvent.customizeEvent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.Question
import com.astery.xapplication.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
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


    private var _questions:List<Question>? = null
    set(value){
        if (value != null)
            _questionsUnit.value = QuestionUnit.createFromList(value)
        field = value
    }

    private val _questionsUnit:MutableLiveData<List<QuestionUnit>?> = MutableLiveData()
    val questions:LiveData<List<QuestionUnit>?>
        get() = _questionsUnit

    init{
        _addEventState.value = JobState.Idle
    }


    fun addEvent(){
        viewModelScope.launch {
            _addEventState.value = JobState.Running

            val event = Event(null, template!!.id, selectedDay!!.time)
            event.template = EventTemplate()
            event.template!!.questions = _questions
            Timber.d("$event")

            repository.addEvent(event)
            _addEventState.value = JobState.Success
        }
    }

    fun loadQuestions(){
        viewModelScope.launch {
            _questions = repository.getQuestionsWithAnswers(template!!.id)
        }
    }

    enum class JobState{
        Idle,
        Running,
        Success
    }
}