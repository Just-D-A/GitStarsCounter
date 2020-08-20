package com.example.gitstarscounter.retrofit2


object SearchRepositoryProvider {
    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(GithubApiService.create())
    }
}