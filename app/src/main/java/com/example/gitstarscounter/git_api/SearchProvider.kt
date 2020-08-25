package com.example.gitstarscounter.git_api

import com.example.gitstarscounter.login.SearchRepository
import com.example.gitstarscounter.stars.SearchStars


object SearchProvider {
    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(GithubApiService.create())
    }

    fun provideSearchStars(): SearchStars {
        return SearchStars(GithubApiService.create())
    }



}

