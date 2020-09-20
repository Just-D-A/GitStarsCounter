package com.example.gitstarscounter

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.gitstarscounter.data.to_rename_2.local.Database
import androidx.work.Configuration

class GitStarsApplication : Application(), Configuration.Provider {
    lateinit var database: Database // to injecting

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, Database::class.java, "database")
            .build()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()

    companion object {
        lateinit var instance: GitStarsApplication
    }
}
