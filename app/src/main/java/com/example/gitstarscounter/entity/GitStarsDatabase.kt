package com.example.gitstarscounter.entity

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gitstarscounter.entity.user.UserDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


object GitStarsDatabase {
    lateinit var db: AppDatabase

    fun createDatabase(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database"
        ).build()
    }

    fun getDatabase(): AppDatabase {
        return db
    }

 /*   private val sRoomDatabaseCallback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute({

                // Populate the database in the background.
                // If you want to start with more words, just add them.
                val dao: UserDao = INSTANCE.wordDao()
                dao.deleteAll()
                var word: Word? = Word("Hello")
                dao.insert(word)
                word = Word("World")
                dao.insert(word)
            })
        }
    }*/
}