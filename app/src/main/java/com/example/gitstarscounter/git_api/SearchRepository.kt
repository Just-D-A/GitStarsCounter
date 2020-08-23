package com.example.gitstarscounter.git_api

import retrofit2.Call


class SearchRepository(val apiService: GithubApiService) {

    fun getUserRepos(userName: String): Call<List<Repository?>?> {
        return apiService.getUserRepos(userName)
    }

    fun getStars(userName: String, repositoryName: String): Call<List<Star?>?> {
        return apiService.getStars(userName, repositoryName, " application/vnd.github.v3.star+json")
    }

}