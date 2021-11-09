package com.astery.xapplication.repository.localDataStorage.dao

import androidx.room.*
import com.astery.xapplication.model.entities.Advise
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.Item

@Dao
interface ArticleDao {
    /** return items with all of its advises  */
    @Query("SELECT * FROM item WHERE id = :itemId")
    suspend fun getItemById(itemId: String): Item

    /** return items with all of its advises  */
    @Query("SELECT * FROM item WHERE parent_id = :parentId")
    suspend fun getItemsByParentId(parentId: String): List<Item>

    
    @Query("SELECT * FROM item")
    suspend fun getItem(): List<Item>
            
    /** return article with all of its items and advises  */
    @Query("SELECT * FROM Article WHERE id = :articleId")
    @Transaction
    suspend fun getArticleById(articleId: String): Article

    @Query("SELECT * from advise where parent_id = :parentId")
    suspend fun getAdvisesForItem(parentId: String): List<Advise>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAdvise(advise: Advise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAdvises(advises: List<Advise>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticles(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItems(items: List<Item>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAdvise(advise: Advise)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAdvises(advises: List<Advise>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateArticle(article: Article)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateArticles(articles: List<Article>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: Item)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItems(items: List<Item>)
}