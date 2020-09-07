package com.example.gitstarscounter.git_api

import com.example.gitstarscounter.login.LoginSearch
import com.example.gitstarscounter.stars.SearchStars

object SearchProvider {
    fun provideSearchRepository(): LoginSearch {
        return LoginSearch(GithubApiService.create())
    }

    fun provideSearchStars(): SearchStars {
        return SearchStars(GithubApiService.create())
    }
}
