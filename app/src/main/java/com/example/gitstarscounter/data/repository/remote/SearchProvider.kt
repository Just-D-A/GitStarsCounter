package com.example.gitstarscounter.data.repository.remote

import com.example.gitstarscounter.ui.screens.login.LoginSearch
import com.example.gitstarscounter.ui.screens.stars.SearchStars

object SearchProvider {
    fun provideSearchRepository(): LoginSearch {
        return LoginSearch(GithubApiService.create())
    }

    fun provideSearchStars(): SearchStars {
        return SearchStars(GithubApiService.create())
    }
}
