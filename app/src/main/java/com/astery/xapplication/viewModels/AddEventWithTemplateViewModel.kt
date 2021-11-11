package com.astery.xapplication.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEventWithTemplateViewModel @Inject constructor(): ViewModel() {
    @set:Inject lateinit var repository: Repository
    private val _templates: MutableLiveData<List<EventTemplate>> = MutableLiveData()
    val templates:LiveData<List<EventTemplate>>
        get() = _templates


    fun loadTemplates(category: EventCategory){
        viewModelScope.launch {
            _templates.value = repository.getEventTemplatesForCategory(category)
        }
    }
}