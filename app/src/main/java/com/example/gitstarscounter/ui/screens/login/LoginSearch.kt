package com.example.gitstarscounter.ui.screens.login

import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.example.gitstarscounter.data.repository.remote.entity.RemoteRepository
import com.example.gitstarscounter.data.repository.remote.entity.resource_remote.ResourceRemote

class LoginSearch(private val apiService: GithubApiService) {
    suspend fun getUserRepos(userName: String, pageNumber: Int): List<RemoteRepository> {
        return apiService.getUserRepos(userName, pageNumber)
    }

    suspend fun getLimitRemaining(): ResourceRemote {
        return apiService.getLimitRemaining()
    }
}
