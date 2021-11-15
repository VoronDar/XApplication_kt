package com.astery.xapplication.ui.fragments.advices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.model.entities.Item
import com.astery.xapplication.model.entities.Question
import com.astery.xapplication.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvicesViewModel @Inject constructor(): ViewModel() {
    @set:Inject lateinit var repository: Repository

    private val _questions: MutableLiveData<List<Question>> = MutableLiveData()

    private val _units: MutableLiveData<List<AdvicesUnit>> = MutableLiveData()
    val units: LiveData<List<AdvicesUnit>>
        get() = _units

    fun setQuestions(questions :List<Question>?){
        _questions.value = questions?: listOf()
    }

    fun loadAdvices(){
        viewModelScope.launch{
            for (i in _questions.value!!){
                if (i.selectedAnswer!!.itemId == null) continue
                i.selectedAnswer!!.item = Item(i.selectedAnswer!!.itemId!!)
                i.selectedAnswer!!.item!!.advices = repository.getAdvicesForItem(i.selectedAnswer!!.itemId!!)
            }
            _units.value = AdvicesUnit.createList(_questions.value!!)
        }
    }

    /**
     * @param position is the position in units, not in questions
     * */
    fun getItemIdForQuestion(position: Int): Int {
        return _questions.value!![units.value!![position].position].selectedAnswer!!.itemId!!
    }

}