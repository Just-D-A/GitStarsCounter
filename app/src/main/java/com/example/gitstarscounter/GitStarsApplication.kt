package com.example.gitstarscounter

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.work.Configuration
import com.example.gitstarscounter.data.repository.local.AppRoomDatabase
import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.example.gitstarscounter.di.DaggerGitStarsCounterComponent
import com.example.gitstarscounter.di.GitStarsCounterComponent
import com.example.gitstarscounter.di.module.AppModule
import com.example.gitstarscounter.di.module.GitApiModule
import com.example.gitstarscounter.di.module.RoomModule

class GitStarsApplication : Application(), Configuration.Provider {
    companion object {
        private const val DATABASE_NAME = "database"
        lateinit var instance: GitStarsApplication
    }

    lateinit var appRoomDatabase: AppRoomDatabase // to injecting
    lateinit var gitApi: GithubApiService // to injecting
    lateinit var gitStarsCounterComponent: GitStarsCounterComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        gitStarsCounterComponent = DaggerGitStarsCounterComponent.builder()
            .appModule(AppModule(this))
            .roomModule(RoomModule(this))
            .build()

        appRoomDatabase = gitStarsCounterComponent.getRoomDatabase()
        gitApi = gitStarsCounterComponent.getGitApi()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
}
