package com.example.gitstarscounter

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.work.Configuration
import com.example.gitstarscounter.data.repository.local.Database
import com.example.gitstarscounter.di.GitStarsCounterComponent

class GitStarsApplication : Application(), Configuration.Provider {
    lateinit var database: Database // to injecting
    lateinit var gitStarsCounterComponent:  GitStarsCounterComponent

  /*  fun getComponent(): AppComponent? {
        return component
    }*/

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, Database::class.java, "database")
            .build()
        //gitStarsCounterComponent = DaggerGitStarsCounterComponent.builder().build()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()

    companion object {
        lateinit var instance: GitStarsApplication
    }
}
