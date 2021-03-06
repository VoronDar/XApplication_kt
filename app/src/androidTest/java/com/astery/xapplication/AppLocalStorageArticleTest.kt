package com.astery.xapplication

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.astery.xapplication.model.entities.AgeTag
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.GenderTag
import com.astery.xapplication.repository.localDataStorage.AppDatabase
import com.astery.xapplication.repository.localDataStorage.AppLocalStorage
import com.astery.xapplication.repository.localDataStorage.LocalStorage
import com.astery.xapplication.runner.TestRunner
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * check RoomLocalStorage
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppLocalStorageArticleTest {

    lateinit var localStorage: LocalStorage
    private lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    @Throws(Exception::class)
    fun createDb() {
        localStorage = AppLocalStorage(
            Room.inMemoryDatabaseBuilder(
                instrumentationContext,
                AppDatabase::class.java
            ).build(), instrumentationContext
        )
    }

    @After
    fun closeDb() {
        runBlocking {
            launch {
                localStorage.deleteArticles()
                localStorage.close()
            }
        }
    }


    /**
     * check getArticlesWithTag()
     * add some articles with different sets of tags
     * check the amount of results for different sets of tags
     * */
    /*
    @Test
    fun checkGetArticlesWithTag() {
        runBlocking {
            launch {
                localStorage.addArticle(
                    Article(1, "name", "description", 10, 12)
                        .addTags(listOf(AgeTag.Adult, GenderTag.Man))
                )
                localStorage.addArticle(
                    Article(2, "adasd", "a asdsa d", 13, 15)
                        .addTags(listOf(AgeTag.Adult))
                )
                localStorage.addArticle(Article(3, "qweqwe", "qwe qwe qw  d", 11, 12))
                localStorage.addArticle(
                    Article(4, "qweqwe", "qwe qwe qw  d", 11, 12).addTags(
                        listOf(
                            AgeTag.Child,
                            GenderTag.Man
                        )
                    )
                )
                localStorage.addArticle(
                    Article(
                        5,
                        "qweqwe",
                        "qwe qwe qw  d",
                        11,
                        12
                    ).addTags(listOf(AgeTag.Child))
                )

                val forMan = localStorage.getArticlesWithTag(listOf(GenderTag.Man))
                val forAdult = localStorage.getArticlesWithTag(listOf(AgeTag.Adult))
                val forWoman = localStorage.getArticlesWithTag(listOf(GenderTag.Woman))
                val forChild = localStorage.getArticlesWithTag(listOf(AgeTag.Child))
                val forAdultMan =
                    localStorage.getArticlesWithTag(listOf(GenderTag.Man, AgeTag.Adult))
                val forEveryone = localStorage.getArticlesWithTag(
                    listOf(
                        GenderTag.Man,
                        AgeTag.Adult,
                        AgeTag.Child
                    )
                )

                // TODO(find a way to test paging)
                assertTrue("found invalid number of results for man = ${forMan.size}", forMan.size == 2)
                assertTrue("found invalid number of results for adult = ${forAdult.size}", forAdult.size == 2)
                assertTrue("found invalid number of results for woman = ${forWoman.size}", forWoman.isEmpty())
                assertTrue("found invalid number of results for adult or man = ${forAdultMan.size}", forAdultMan.size == 3)
                assertTrue("found invalid number of results for child = ${forChild.size}", forChild.size == 2)
                assertTrue("found invalid number of results for everyone = ${forEveryone.size}", forEveryone.size == 4)

            }
        }
    }*/

}