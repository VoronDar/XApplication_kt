package com.astery.xapplication.ui.fragments.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.FeedBackState
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.repository.feetback.OnFeedbackListener
import com.astery.xapplication.ui.pageFeetback.FeedBackStorage
import com.astery.xapplication.ui.pageFeetback.advice.OnAdviceFeetBackListenerImpl
import com.astery.xapplication.ui.pageFeetback.advice.OnAdviceFeetbackListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor() : ViewModel(), HasPresentable, OnFeedbackListener {
    @set:Inject
    lateinit var repository: Repository



    /** selected page (article or item) */
    private val _element = MutableLiveData<Presentable>()
    override val element: LiveData<Presentable>
        get() = _element

    private val _article = MutableLiveData<Article>()
    val article: LiveData<Article>
        get() = _article

    var feedBackAdviceListener: OnAdviceFeetbackListener? = null
    private val _feedBackArticleStorage: MutableLiveData<FeedBackStorage> = MutableLiveData()
    val feedBackArticleStorage:LiveData<FeedBackStorage>
    get() = _feedBackArticleStorage

    fun loadArticle(id: Int) {
        feedBackAdviceListener = OnAdviceFeetBackListenerImpl(viewModelScope, repository)

        viewModelScope.launch {
            // load article
            _article.value = repository.getArticle(id)
            val ap = ArticlePresentable(article.value!!)
            _element.value = ap
            _feedBackArticleStorage.value =
                FeedBackStorage(ap.likes, ap.dislikes, ap.feedBackState!!, this@ArticleViewModel)

            // load items
            article.value!!.items = repository.getItemsForArticle(id)
            _article.value = article.value
        }
    }


    /** change presentable (selected page) to article */
    fun selectArticle() {
        _element.value = ArticlePresentable(article.value!!)
    }

    /** change presentable (selected page) to item*/
    fun selectItem(pos: Int) {
        val item = article.value!!.items!![pos]
        _element.value = ItemPresentable(item)

        if (item.advices == null) {
            viewModelScope.launch {
                item.advices = repository.getAdvicesForItem(item.id!!)
                _element.value = ItemPresentable(item)
            }
        }
    }




    // MARK: feedback for article
    override fun onLike() {
        reloadStorage()
        viewModelScope.launch {
            repository.likeArticle(article.value!!.id)
        }
    }

    override fun onCancelLike() {
        reloadStorage()
        viewModelScope.launch {
            repository.cancelLikeArticle(article.value!!.id)
        }
    }

    override fun onDislike() {
        reloadStorage()
        viewModelScope.launch {
            repository.dislikeArticle(article.value!!.id)
        }
    }

    override fun onCancelDislike() {
        reloadStorage()
        viewModelScope.launch {
            repository.cancelDislikeArticle(article.value!!.id)
        }
    }

    override fun onChangeFeetBackState(feedBackState: FeedBackState) {
        reloadStorage()
        viewModelScope.launch {
            repository.changeFeetBackStateForArticle(article.value!!.id, feedBackState)
        }
    }

    private fun reloadStorage(){
        _feedBackArticleStorage.value = _feedBackArticleStorage.value
    }

}
