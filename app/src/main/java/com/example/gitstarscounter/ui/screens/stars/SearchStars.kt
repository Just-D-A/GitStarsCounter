package com.example.gitstarscounter.ui.screens.stars

import com.example.gitstarscounter.data.remote.GithubApiService
import com.example.gitstarscounter.data.remote.entity.StarRemote
import retrofit2.Call

class SearchStars(val apiService: GithubApiService) {
    fun getStars(
        userName: String,
        repositoryName: String,
        pageNumber: Int
    ): Call<List<StarRemote?>?> {
        return apiService.getStars(
            userName,
            repositoryName,
            URL_API,
            pageNumber,
            MAX_ELEMENTS_FROM_API
        )
    }

    companion object {
        const val MAX_ELEMENTS_FROM_API = 100
        const val URL_API = "application/vnd.github.v3.star+json" //header for get param starred_at
    }
}
