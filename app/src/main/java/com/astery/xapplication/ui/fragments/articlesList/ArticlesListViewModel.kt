package com.astery.xapplication.ui.fragments.articlesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
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

    private var articlesFlow: Flow<PagingData<Article>>? = null

    fun requestFlow(searchSequence: String, filters: List<ArticleTag>): Flow<PagingData<Article>> {
        //TODO(сделать сортировку по дате/важности (когда-нибудь). Важность расчитывается из 2 пунктов - соотношение лайков к дизлайкам и количество оценок всего)
        articlesFlow = Pager(PagingConfig(pageSize = PAGED_SIZE, maxSize = 12)) {
            repository.getArticles(cleanSearchSequence(searchSequence), filters)
        }.flow
        //articlesFlow = Pager(PagingConfig(4)){ FirestorePagingSource(FirebaseFirestore.getInstance()) }.flow.cachedIn(viewModelScope)

        return articlesFlow!!
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
