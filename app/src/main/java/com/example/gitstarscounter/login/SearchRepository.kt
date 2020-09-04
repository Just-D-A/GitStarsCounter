package com.example.gitstarscounter.login

import com.example.gitstarscounter.git_api.GithubApiService
import com.example.gitstarscounter.git_api.RepositoryModel
import retrofit2.Call
import retrofit2.http.Query


class SearchRepository(val apiService: GithubApiService) {
    fun getUserRepos(userName: String,  pageNumber: Int): Call<List<RepositoryModel?>?> {
        return apiService.getUserRepos(userName, pageNumber)
    }
}