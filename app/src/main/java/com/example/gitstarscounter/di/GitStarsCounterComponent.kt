package com.example.gitstarscounter.di

import android.app.Application
import androidx.work.PeriodicWorkRequest
import com.example.gitstarscounter.data.repository.local.AppRoomDatabase
import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.example.gitstarscounter.data.repository.remote.SearchProvider
import com.example.gitstarscounter.di.module.*
import com.example.gitstarscounter.ui.screens.login.LoginActivity
import com.example.gitstarscounter.ui.screens.login.LoginPresenter
import com.example.gitstarscounter.ui.screens.stars.StarsPresenter
import dagger.Component

import javax.inject.Singleton

@Singleton
@Component(modules = [GitApiModule::class, AppModule::class, RoomModule::class, WorkerModule::class, RepositoryModule::class])
interface GitStarsCounterComponent {
    fun getGitApi(): GithubApiService
    fun getApplication(): Application
    fun getRoomDatabase(): AppRoomDatabase
    fun inject(loginActivity: LoginActivity) // inject workers
    fun inject(searchProvider: SearchProvider) // inject retrofit
    fun inject(loginPresenter: LoginPresenter) // inject LoginRepository
    fun inject(starPresenter: StarsPresenter) // inject StarRepository
}
