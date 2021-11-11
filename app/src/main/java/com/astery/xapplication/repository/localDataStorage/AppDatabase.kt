package com.astery.xapplication.repository.localDataStorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.astery.xapplication.model.entities.*
import com.astery.xapplication.repository.localDataStorage.dao.ArticleDao
import com.astery.xapplication.repository.localDataStorage.dao.EventsDao

@Database(
    entities = [Article::class, Item::class, Advise::class, Answer::class,
        EventTemplate::class, Event::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    //abstract fun questionDao(): QuestionDao
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
                    "database_4")
                    //.addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}