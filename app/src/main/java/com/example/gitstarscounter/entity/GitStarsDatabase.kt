package com.example.gitstarscounter.entity

import android.content.Context
import androidx.room.Room

object GitStarsDatabase {
    lateinit var db: AppDatabase

    fun createDatabase(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database"
        )
            .build()
    }

    fun getDatabase(): AppDatabase {
        return db
    }
}
