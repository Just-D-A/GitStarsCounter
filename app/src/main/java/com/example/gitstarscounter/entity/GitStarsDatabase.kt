package com.example.gitstarscounter.entity

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


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