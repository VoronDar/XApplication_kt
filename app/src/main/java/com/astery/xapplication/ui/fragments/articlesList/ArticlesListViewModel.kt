package com.astery.xapplication.ui.fragments.articlesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.astery.xapplication.model.entities.AgeTag
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.ArticleTag
import com.astery.xapplication.model.entities.GenderTag
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.ui.fragments.articlesList.model.ArticlePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
            for (i in 0..8) {
                val article = Article(i, "name $i", "body", 12, 13)
                article.tags = listOf(GenderTag.Man, AgeTag.Adult)
                repository.localStorage.addArticleWithTag(article)
            }
            for (i in 0..6) {
                val article = Article(i +  100, "woman $i", "body", 12, 13)
                article.tags = listOf(GenderTag.Woman)
                repository.localStorage.addArticleWithTag(article)
            }

            for (i in 0..5) {
                val article = Article(i + 200, "keyword tips $i", "key collection", 12, 13)
                article.tags = listOf(GenderTag.Woman)
                repository.localStorage.addArticleWithTag(article)
            }

            for (i in 0..5) {
                val article = Article(i + 200, "keyword tips $i", "key collection", 12, 13)
                article.tags = listOf(GenderTag.Woman)
                repository.localStorage.addArticleWithTag(article)
            }
        }
    }
    private var articlesFlow:Flow<PagingData<Article>>? = null

    fun requestFlow(searchSequence:String, filters:List<ArticleTag>): Flow<PagingData<Article>> {
        //TODO(сделать сортировку по дате/важности (когда-нибудь). Важность расчитывается из 2 пунктов - соотношение лайков к дизлайкам и количество оценок всего)
        articlesFlow = Pager(PagingConfig(pageSize = PAGED_SIZE, maxSize = 12, enablePlaceholders = true)){
            repository.getArticles(searchSequence, filters) }.flow.cachedIn(viewModelScope)
        return articlesFlow!!
    }


    companion object{
        const val PAGED_SIZE = 4
    }
}
