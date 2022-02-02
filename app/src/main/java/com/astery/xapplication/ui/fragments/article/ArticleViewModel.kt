package com.astery.xapplication.ui.fragments.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.Question
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.repository.feetback.OnFeedbackListener
import com.astery.xapplication.ui.pageFeetback.FeedBackStorage
import com.astery.xapplication.ui.pageFeetback.advice.OnAdviceFeedbackListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor() : ViewModel(), HasPresentable, OnFeedbackListener {
    @set:Inject
    lateinit var repository: Repository


    /** selected page (article or item) */
    private val _element = MutableLiveData<Result<Presentable>>()
    override val element: LiveData<Result<Presentable>>
        get() = _element


    private val _presentable = MutableLiveData<Presentable>()
    override val presentable: LiveData<Presentable>
        get() = _presentable


    private val _article = MutableLiveData<Article>()
    val article: LiveData<Article>
        get() = _article

    var feedBackAdviceListener: OnAdviceFeedbackListener? = null
    private val _feedBackArticleStorage: MutableLiveData<FeedBackStorage> = MutableLiveData()
    val feedBackArticleStorage: LiveData<FeedBackStorage>
        get() = _feedBackArticleStorage

    fun setArticle(article: Article) {

        viewModelScope.launch {
            _article.value = article
            val ap = ArticlePresentable(article)
            setPresentable(ap)

            _feedBackArticleStorage.value =
                FeedBackStorage(ap.likes, ap.dislikes, ap.feedBackState, this@ArticleViewModel)

            // TODO(maybe add an indicator of error)
            article.items = repository.getItemsForArticle(article.id).getOrNull()
            // reload liveData to update UI
            _article.value = article
        }
    }


    /** change presentable (selected page) to article */
    fun selectArticle() {
        setPresentable(ArticlePresentable(article.value!!))
    }

    /** change presentable (selected page) to item*/
    fun selectItem(pos: Int) {
        val item = article.value!!.items!![pos]
        setPresentable(ItemPresentable(item))
        feedBackAdviceListener =
            OnAdviceFeedbackListener(listOf(Question(item)), viewModelScope, repository)

        if (item.advices == null) {
            viewModelScope.launch {
                item.advices = repository.getAdvicesForItem(item.id).getOrNull()
                if (item.advices?.isNullOrEmpty() == false)
                    setPresentable(ItemPresentable(item))
            }
        }
        viewModelScope.launch {
            item.image = repository.getImageForItem(item.id)
            if (item.image != null)
                setPresentable(ItemPresentable(item))
        }
    }

    private fun setPresentable(value: Presentable) {
        _element.value = Result.success(value)
        _presentable.value = value
    }


    // MARK: feedback for article
    override fun onLike() {
        reloadStorage()
        viewModelScope.launch {
            repository.likeArticle(
                article.value!!.id,
                article.value!!.likes,
                article.value!!.dislikes
            )
        }
    }

    override fun onCancelLike() {
        reloadStorage()
        viewModelScope.launch {
            repository.cancelLikeArticle(
                article.value!!.id,
                article.value!!.likes,
                article.value!!.dislikes
            )
        }
    }

    override fun onDislike() {
        reloadStorage()
        viewModelScope.launch {
            repository.dislikeArticle(
                article.value!!.id,
                article.value!!.likes,
                article.value!!.dislikes
            )
        }
    }

    override fun onCancelDislike() {
        reloadStorage()
        viewModelScope.launch {
            repository.cancelDislikeArticle(
                article.value!!.id,
                article.value!!.likes,
                article.value!!.dislikes
            )
        }
    }

    private fun reloadStorage() {
        _feedBackArticleStorage.value = _feedBackArticleStorage.value
    }

}
