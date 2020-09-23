package com.example.gitstarscounter.di

import android.app.Application
import com.example.gitstarscounter.data.repository.local.AppRoomDatabase
import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.example.gitstarscounter.di.module.AppModule
import com.example.gitstarscounter.di.module.GitApiModule
import com.example.gitstarscounter.di.module.RoomModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GitApiModule::class, AppModule::class, RoomModule::class])
interface GitStarsCounterComponent {
    fun getGitApi(): GithubApiService
    fun getApplication(): Application
    fun getRoomDatabase(): AppRoomDatabase
    fun inject(application: Application)
}
