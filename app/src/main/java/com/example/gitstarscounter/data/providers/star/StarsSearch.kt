package com.example.gitstarscounter.data.providers.star

import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.example.gitstarscounter.data.repository.remote.entity.remote.RemoteStar

class StarsSearch(private val apiService: GithubApiService) {
    companion object {
        const val MAX_ELEMENTS_FROM_API = 100
        const val URL_API = "application/vnd.github.v3.star+json" //header for get param starred_at
    }

    suspend fun getStars(
        userName: String,
        repositoryName: String,
        pageNumber: Int
    ): List<RemoteStar> {
        return apiService.getStars(
            userName,
            repositoryName,
            URL_API,
            pageNumber,
            MAX_ELEMENTS_FROM_API
        )
    }
}
