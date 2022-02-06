package com.astery.xapplication.ui.fragments.articlesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.ArticleTag
import com.astery.xapplication.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesListViewModel @Inject constructor() : ViewModel() {
    @set:Inject
    lateinit var repository: Repository

    // Я знаю, что так нельзя делать. Но пока я не сделаю полнотекстовый поиск из fb оно будет вот настолько криво
    private val _articlesFlow:MutableLiveData<Flow<PagingData<Article>>> = MutableLiveData()
    val articlesFlow: LiveData<Flow<PagingData<Article>>?>
    get() = _articlesFlow


    fun requestFlow(searchSequence: String, filters: List<ArticleTag>){

        viewModelScope.launch {
            if (filters.isEmpty()) repository.updateArticlesWithTags(ArticleTag.getAllTags())
            else repository.updateArticlesWithTags(filters)

            //TODO(сделать сортировку по дате/важности (когда-нибудь). Важность расчитывается из 2 пунктов - соотношение лайков к дизлайкам и количество оценок всего)
            _articlesFlow.value = Pager(PagingConfig(pageSize = PAGED_SIZE, maxSize = 12)) {
                repository.getArticles(cleanSearchSequence(searchSequence), filters)
            }.flow
        }
    }

    private fun cleanSearchSequence(searchSequence: String): String {
        return searchSequence.replace("\"", "").lowercase()
    }

    fun getImage(article: Article, articlePosition: Int, adapter: ArticlesListAdapter) {
        viewModelScope.launch {
            article.image = repository.getImageForArticle(article.id)
            if (article.image != null) {
                var isNotified = false
                do {
                    try {
                        adapter.notifyItemChanged(articlePosition)
                        isNotified = true
                    } catch (e: Exception) {
                        delay(100)
                    }
                } while (!isNotified)
            }
        }
    }


    companion object {
        const val PAGED_SIZE = 4
    }
}
