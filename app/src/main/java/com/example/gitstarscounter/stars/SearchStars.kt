package com.example.gitstarscounter.stars

import com.example.gitstarscounter.git_api.GithubApiService
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.Star
import retrofit2.Call


class SearchStars(val apiService: GithubApiService) {

    /*fun getUserRepos(userName: String): Call<List<Repository?>?> {
        return apiService.getUserRepos(userName)
    }*/

    fun getStars(userName: String, repositoryName: String): Call<List<Star?>?> {
        return apiService.getStars(userName, repositoryName, " application/vnd.github.v3.star+json")
    }

}