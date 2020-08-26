package com.example.gitstarscounter.stars

import com.example.gitstarscounter.git_api.GithubApiService
import com.example.gitstarscounter.git_api.Star
import retrofit2.Call


class SearchStars(val apiService: GithubApiService) {

    fun getStars(userName: String, repositoryName: String, pageNumber: Int): Call<List<Star?>?> {
        return apiService.getStars(userName, repositoryName, "application/vnd.github.v3.star+json", pageNumber, 100)
    }

    fun getStarsCount(userName: String, repositoryName: String): Int {
        return apiService.getStarsCount(userName, repositoryName)
    }

}