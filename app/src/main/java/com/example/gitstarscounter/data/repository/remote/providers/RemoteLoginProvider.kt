package com.example.gitstarscounter.data.repository.remote.providers

import com.example.gitstarscounter.data.repository.remote.SearchProvider
import com.example.gitstarscounter.data.repository.remote.entity.resource_remote.ResourceRemote
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.data.providers.login.LoginProvider

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST")
class RemoteLoginProvider : LoginProvider {
    companion object {
        private const val TAG = "REMOTE_LOGIN_PROVIDER"
    }

    override suspend fun getUsersRepositories(
        userName: String,
        pageNumber: Int
    ): List<Repository> {
        val repository = SearchProvider.provideSearchRepository()

        return repository.getUserRepos(userName, pageNumber)
    }

    suspend fun loadMoreRepositories(
        userName: String,
        pageNumber: Int
    ): List<Repository> {
        val repository = SearchProvider.provideSearchRepository()

        return repository.getUserRepos(userName, pageNumber)
    }

    suspend fun getLimitRemaining(): ResourceRemote {
        val loginSearch = SearchProvider.provideSearchRepository()

        return loginSearch.getLimitRemaining()
    }
}
