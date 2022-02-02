package com.astery.xapplication.ui.fragments.itemForAnswer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.model.entities.Item
import com.astery.xapplication.model.entities.Question
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.ui.fragments.article.HasPresentable
import com.astery.xapplication.ui.fragments.article.ItemPresentable
import com.astery.xapplication.ui.fragments.article.Presentable
import com.astery.xapplication.ui.loadingState.UnexpectedBugException
import com.astery.xapplication.ui.pageFeetback.advice.OnAdviceFeedbackListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor() : ViewModel(), HasPresentable {
    @set:Inject
    lateinit var repository: Repository

    private val _element = MutableLiveData<Result<Presentable>>()
    override val element: LiveData<Result<Presentable>>
        get() = _element

    private val _presentable = MutableLiveData<Presentable>()
    override val presentable: LiveData<Presentable>
        get() = _presentable


    var item: Item? = null

    /** load required parts of item:body and name */
    fun loadItemBody() {
        viewModelScope.launch {
            if (item == null) {
                _element.value = Result.failure(UnexpectedBugException())
                cancel()
                return@launch
            }

            val it = repository.setItemBody(item!!)
            if (it.isSuccess) {
                _element.value = Result.success(ItemPresentable(it.getOrThrow()))
                feedbackListener = OnAdviceFeedbackListener(listOf(Question(it.getOrThrow())), viewModelScope, repository)
            }
            else
                _element.value = Result.failure(it.exceptionOrNull()!!)

            _presentable.value = element.value!!.getOrNull()
        }

    }


    var feedbackListener: OnAdviceFeedbackListener? = null
}
