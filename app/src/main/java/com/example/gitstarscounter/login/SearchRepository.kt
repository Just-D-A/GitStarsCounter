package com.example.gitstarscounter.login

import com.example.gitstarscounter.git_api.GithubApiService
import com.example.gitstarscounter.git_api.Repository
import retrofit2.Call


class SearchRepository(val apiService: GithubApiService) {
    fun getUserRepos(userName: String): Call<List<Repository?>?> {
        return apiService.getUserRepos(userName)
    }
}