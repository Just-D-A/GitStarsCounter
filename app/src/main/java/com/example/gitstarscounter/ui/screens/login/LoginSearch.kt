package com.example.gitstarscounter.ui.screens.login

import com.example.gitstarscounter.data.remote.GithubApiService
import com.example.gitstarscounter.data.remote.entity.RepositoryRemote
import com.example.gitstarscounter.data.remote.entity.ResourceRemote
import retrofit2.Call

class LoginSearch(private val apiService: GithubApiService) {
    fun getUserRepos(userName: String, pageNumber: Int): Call<List<RepositoryRemote?>?> {
        return apiService.getUserRepos(userName, pageNumber)
    }

    fun getLimitRemaining(): Call<ResourceRemote> {
        return apiService.getLimitRemaining()
    }
}
