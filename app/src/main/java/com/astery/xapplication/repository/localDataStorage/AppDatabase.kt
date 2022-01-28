package com.astery.xapplication.repository.localDataStorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.astery.xapplication.model.entities.*
import com.astery.xapplication.repository.localDataStorage.dao.ArticleDao
import com.astery.xapplication.repository.localDataStorage.dao.EventsDao
import com.astery.xapplication.repository.localDataStorage.dao.DesireDao

@Database(
    entities = [Article::class, Item::class, Advice::class, Answer::class,
        EventTemplate::class, Event::class, ArticleAndTag::class, AnswerAndEvent::class,
               Question::class, ArticleFts::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun desireDao(): DesireDao
    //abstract fun faqDao(): FaqDao
    abstract fun eventDao(): EventsDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "database_15")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}