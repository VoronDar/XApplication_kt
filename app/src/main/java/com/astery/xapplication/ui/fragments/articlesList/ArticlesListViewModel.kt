package com.astery.xapplication.ui.fragments.articlesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.GenderTag
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.ui.fragments.articlesList.model.ArticlePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.sql.DataSource

@HiltViewModel
class ArticlesListViewModel @Inject constructor() : ViewModel(){
    @set:Inject
    lateinit var repository: Repository

    init{
        viewModelScope.launch {
            for (i in 0..10) {
                val article = Article(i, "name $i", "body", 12, 13)
                article.tags = listOf(GenderTag.Man)
                repository.localStorage.addArticleWithTag(article)
            }
        }
    }

    /*
    var articlesFlow = Pager(PagingConfig(PAGED_SIZE)){ ArticlePagingSource(listOf())}
        .flow
        .cachedIn(viewModelScope)

    fun setArticleFlow(tags:List<Int>){
        articlesFlow = Pager(PagingConfig(PAGED_SIZE)) { ArticlePagingSource(tags) }
            .flow
            .cachedIn(viewModelScope)
    }*/

    var articlesFlow = Pager(PagingConfig(pageSize = PAGED_SIZE, maxSize = 30, enablePlaceholders = true)){
        repository.getArticles() }.flow.cachedIn(viewModelScope)

    companion object{
        const val PAGED_SIZE = 5
    }
}
