package com.astery.xapplication.repository.localDataStorage.dao

import androidx.room.*
import com.astery.xapplication.model.entities.*

@Dao
interface ArticleDao {
    /** return items with all of its advises  */
    @Query("SELECT * FROM item WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): Item

    /** return items with all of its advises  */
    @Query("SELECT * FROM item WHERE parent_id = :parentId ORDER BY pagePosition")
    suspend fun getItemsByParentId(parentId: Int): List<Item>

    @Query("SELECT body, name FROM item WHERE id = :itemId")
    suspend fun getItemBody(itemId: Int):Item
            
    /** return article with all of its items and advises  */
    @Query("SELECT * FROM Article WHERE id = :articleId")
    @Transaction
    suspend fun getArticleById(articleId: Int): Article

    /** return articles */
    @Query("SELECT Article.id, likes, dislikes, name FROM Article INNER JOIN ArticleAndTag ON Article.id == ArticleAndTag.articleId" +
            "  AND ArticleAndTag.tagId IN (:tags) GROUP BY Article.ID")
    suspend fun getArticlesWithTag(tags: List<Int>): List<Article>

    /** return articles */
    @Query("SELECT Article.id, likes, dislikes, name FROM Article INNER JOIN ArticleAndTag ON Article.id == ArticleAndTag.articleId  " +
            "AND ArticleAndTag.tagId IN (:tags) AND (name LIKE '%' || :key || '%' OR body LIKE '%' || :key || '%') GROUP BY Article.ID")
    suspend fun getArticlesWIthTagAndKeyWord(tags: List<Int>, key:String): List<Article>

    @Query("SELECT * from advice where itemId = :parentId")
    suspend fun getAdvisesForItem(parentId: Int): List<Advice>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAdvise(advice: Advice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAdvises(advice: List<Advice>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTagRelation(articleAndTag: ArticleAndTag)

    @Transaction
    suspend fun addArticleWithTags(article: Article){
        addArticle(article)
        if (article.tags!= null){
            for (i in article.tags!!){
                addTagRelation(ArticleAndTag(article.id, i.id))
            }
        }
    }



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticles(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItems(items: List<Item>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAdvise(advice: Advice)

    @Query("UPDATE advice SET feedback = :feedBackState WHERE id = :adviceId")
    suspend fun updateAdviceFeedbackState(adviceId:Int, feedBackState: FeedBackState)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAdvises(advice: List<Advice>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateArticle(article: Article)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateArticles(articles: List<Article>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: Item)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItems(items: List<Item>)

    @Query("DELETE FROM ArticleAndTag")
    suspend fun deleteArticleAndTagRelations()

    @Query("DELETE FROM Article")
    suspend fun deleteArticles()

    @Transaction
    suspend fun deleteArticlesWithTags(){
        deleteArticles()
        deleteArticleAndTagRelations()
    }


}