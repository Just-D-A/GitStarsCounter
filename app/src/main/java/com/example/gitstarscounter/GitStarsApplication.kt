package com.example.gitstarscounter

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.work.Configuration
import com.example.gitstarscounter.data.repository.local.AppRoomDatabase
import com.example.gitstarscounter.di.DaggerGitStarsCounterComponent
import com.example.gitstarscounter.di.GitStarsCounterComponent

class GitStarsApplication : Application(), Configuration.Provider {
    lateinit var appRoomDatabase: AppRoomDatabase // to injecting
    lateinit var gitStarsCounterComponent:  GitStarsCounterComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        appRoomDatabase = Room.databaseBuilder(this, AppRoomDatabase::class.java, "database")
            .build()
        gitStarsCounterComponent = DaggerGitStarsCounterComponent.builder().build()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()

    companion object {
        lateinit var instance: GitStarsApplication
    }
}
