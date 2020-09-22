package com.example.gitstarscounter.di

import android.app.Application
import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.example.gitstarscounter.di.module.GitApiModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GitApiModule::class])
interface GitStarsCounterComponent {
    fun getGitApi(): GithubApiService
    //fun getApplication(): Application
    //fun getRoomDatebase()
}

