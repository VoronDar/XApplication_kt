package com.astery.xapplication.ui.fragments.itemForAnswer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.model.entities.Item
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.ui.fragments.article.HasPresentable
import com.astery.xapplication.ui.fragments.article.ItemPresentable
import com.astery.xapplication.ui.fragments.article.Presentable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor() : ViewModel(), HasPresentable {
    @set:Inject
    lateinit var repository: Repository

    private val _element = MutableLiveData<Presentable>()
    override val element: LiveData<Presentable>
        get() = _element

    var item: Item? = null

    /** load required parts of item:body and name */
    fun loadItemBody() {
        viewModelScope.launch {
            if (item == null) cancel()

            if (element.value == null)
                _element.value = ItemPresentable(repository.setItemBody(item!!))
        }
    }
}
