package com.astery.xapplication.ui.fragments.articlesList.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.repository.Repository
import timber.log.Timber
import javax.inject.Inject

class ArticlePagingSource(private val tags:List<Int>): PagingSource<Int, Article>() {
    @set:Inject
    lateinit var repository: Repository
    private var initialPageIndex:Int = -1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        Timber.d("load from pafing source")
        val articles = repository.localStorage.getArticlesWithTagPaged(tags, params.loadSize)
        if (initialPageIndex == -1){
            initialPageIndex = articles[0].id
        }
        val position:Int = if (params.key == null) initialPageIndex else params.key!!
        Timber.d(articles.toString())

        return LoadResult.Page(
            data = articles,
            prevKey = if (position == initialPageIndex) null else (position - 1),
            nextKey = if (articles.isNullOrEmpty()) null else (position + 1)
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? = null
}