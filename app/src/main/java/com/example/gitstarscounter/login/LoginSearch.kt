package com.example.gitstarscounter.login

import com.example.gitstarscounter.git_api.GithubApiService
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.ResourceModel
import retrofit2.Call

class LoginSearch(val apiService: GithubApiService) {
    fun getUserRepos(userName: String,  pageNumber: Int): Call<List<RepositoryModel?>?> {
        return apiService.getUserRepos(userName, pageNumber)
    }
    fun getLimitRemaining(): Call<ResourceModel> {
        return  apiService.getLimitRemaining()
    }
}
