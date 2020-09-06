package com.example.gitstarscounter.entity

import android.app.Application
import androidx.room.Room

class App : Application() {
    private var database: AppDatabase? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .build()
    }

    fun getDatabase(): AppDatabase? {
        return database
    }

    companion object {
        var instance: App? = null
    }
}
