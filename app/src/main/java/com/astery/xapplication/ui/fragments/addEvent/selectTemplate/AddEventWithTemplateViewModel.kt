package com.astery.xapplication.ui.fragments.addEvent.selectTemplate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.model.entities.EventTemplate
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddEventWithTemplateViewModel @Inject constructor(): ViewModel() {
    @set:Inject lateinit var repository: Repository
    private val _templates: MutableLiveData<List<EventTemplate>> = MutableLiveData()
    val templates:LiveData<List<EventTemplate>>
        get() = _templates


    fun loadTemplates(category: EventCategory){
        if (templates.value == null) {
            viewModelScope.launch {
                _templates.value = repository.getEventTemplatesForCategory(category)
            }
        }
    }
    fun loadImages(a:EventTemplateAdapter){
        for (i in templates.value!!.indices){
            viewModelScope.launch {
                templates.value!![i].image =
                    repository.getImageForEventTemplate(templates.value!![i])
                a.notifyItemChanged(i)
            }
        }
    }
}