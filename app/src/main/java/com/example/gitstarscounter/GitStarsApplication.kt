package com.example.gitstarscounter

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.gitstarscounter.data.repository.local.AppRoomDatabase
import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.example.gitstarscounter.di.DaggerGitStarsCounterComponent
import com.example.gitstarscounter.di.GitStarsCounterComponent
import com.example.gitstarscounter.di.module.AppModule
import com.example.gitstarscounter.di.module.RoomModule
import com.omega_r.libs.omegatypes.image.GlideImagesProcessor
import com.omega_r.libs.omegatypes.image.ImageProcessors

class GitStarsApplication : Application(), Configuration.Provider {
    companion object {
        lateinit var instance: GitStarsApplication
    }

    lateinit var gitStarsCounterComponent: GitStarsCounterComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        GlideImagesProcessor.setAsCurrentImagesProcessor()

        gitStarsCounterComponent = DaggerGitStarsCounterComponent.builder()
            .appModule(AppModule(this))
            .roomModule(RoomModule(this))
            .build()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
}
