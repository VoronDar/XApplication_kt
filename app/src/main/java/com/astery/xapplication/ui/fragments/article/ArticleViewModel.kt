package com.astery.xapplication.ui.fragments.article

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.Item
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.viewModels.ItemViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(): ViewModel(), HasPresentable {
    @set:Inject lateinit var repository: Repository

    /** selected page (article or item) */
    private val _element = MutableLiveData<Presentable>()
    override val element:LiveData<Presentable>
        get() = _element

    private val _article = MutableLiveData<Article>()
    val article:LiveData<Article>
        get() = _article


    fun loadArticle(id:Int){
        viewModelScope.launch {
            // load article
            _article.value = repository.getArticle(id)
            _element.value = ArticlePresentable(article.value!!)

            // load items
            article.value!!.items = repository.getItemsForArticle(id)
            _article.value = article.value
        }
    }


    /** change presentable (selected page) to article */
    fun selectArticle(){
        _element.value = ArticlePresentable(article.value!!)
    }

    /** change presentable (selected page) to item*/
    fun selectItem(pos:Int){
        val item = article.value!!.items!![pos]
        _element.value = ItemPresentable(item)

        if (item.advices == null) {
            viewModelScope.launch {
                item.advices = repository.getAdvicesForItem(item.id)
                _element.value = ItemPresentable(item)
            }
        }
    }

}
