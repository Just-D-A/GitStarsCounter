package com.example.gitstarscounter.di

import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.example.gitstarscounter.ui.screens.login.LoginSearch
import com.example.gitstarscounter.ui.screens.stars.SearchStars
import dagger.Component

@Component
class Component {
    fun provideSearchRepository(): LoginSearch {
        return LoginSearch(GithubApiService.create())
    }

    fun provideSearchStars(): SearchStars {
        return SearchStars(GithubApiService.create())
    }
}