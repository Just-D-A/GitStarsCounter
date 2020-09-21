package com.example.gitstarscounter.di

import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.example.gitstarscounter.di.module.GitApiModule
import dagger.Component

@Component(modules = [GitApiModule::class])
interface GitStarsCounterComponent {
    fun getGitApi(): GithubApiService
}

