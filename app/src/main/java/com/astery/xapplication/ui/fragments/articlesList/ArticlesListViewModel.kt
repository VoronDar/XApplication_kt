package com.astery.xapplication.ui.fragments.articlesList

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.astery.xapplication.model.entities.*
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.repository.remoteDataStorage.FirestorePagingSource
import com.astery.xapplication.ui.fragments.articlesList.model.ArticlePagingSource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.sql.DataSource

@HiltViewModel
class ArticlesListViewModel @Inject constructor() : ViewModel(){
    @set:Inject
    lateinit var repository: Repository

    private var articlesFlow:Flow<PagingData<Article>>? = null

    fun requestFlow(searchSequence:String, filters:List<ArticleTag>): Flow<PagingData<Article>> {
        //TODO(сделать сортировку по дате/важности (когда-нибудь). Важность расчитывается из 2 пунктов - соотношение лайков к дизлайкам и количество оценок всего)
        articlesFlow = Pager(PagingConfig(pageSize = PAGED_SIZE, maxSize = 12)){
            repository.getArticles(cleanSearchSequence(searchSequence), filters) }.flow.cachedIn(viewModelScope)
        //articlesFlow = Pager(PagingConfig(4)){ FirestorePagingSource(FirebaseFirestore.getInstance()) }.flow.cachedIn(viewModelScope)

        return articlesFlow!!
    }

    private fun cleanSearchSequence(searchSequence: String):String{
        return searchSequence.replace("\"", "").lowercase()
    }

    fun getImage(article:Article, articlePosition:Int, adapter:ArticlesListAdapter){
        viewModelScope.launch {
            Timber.d("want to notify ${articlePosition} ${article.id}")
            article.image = repository.getImageForArticle(article.id)
            if (article.image != null) {
                var isNotified = false
                do {
                    try {
                        adapter.notifyItemChanged(articlePosition)
                        isNotified = true
                        Timber.d("${articlePosition} - ${article.id} notified")
                    } catch (e: Exception) {
                        Timber.d("failed to render an image")
                        delay(100)
                    }
                } while (!isNotified)
            }
        }
    }



    companion object{
        const val PAGED_SIZE = 4
    }
}
