package com.astery.xapplication.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.astery.xapplication.model.entities.Item
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.ui.fragments.article.ArticleViewModel
import com.astery.xapplication.ui.fragments.article.HasPresentable
import com.astery.xapplication.ui.fragments.article.Presentable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(): ViewModel(), HasPresentable {
    @set:Inject lateinit var repository: Repository

    private val _element = MutableLiveData<Presentable>()
    override val element:LiveData<Presentable>
        get() = _element

    /*
    fun loadEventBody(id:Int){
        viewModelScope.launch {
            if (element.value == null)
                _element.value = ItemPresentable(repository.getItem(id))
        }
    }

     */

}
