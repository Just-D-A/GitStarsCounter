package com.example.gitstarscounter.data.to_rename_2.remote.providers

import com.example.gitstarscounter.data.to_rename_2.remote.SearchProvider
import com.example.gitstarscounter.data.to_rename_2.remote.entity.resource_remote.ResourceRemote
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.data.for_providers.login.LoginProvider

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST")
class RemoteLoginProvider : LoginProvider {
    companion object {
        const val TAG = "REMOTE_LOGIN_PROVIDER"
    }

    override suspend fun getUsersRepositories(
        userName: String,
        pageNumber: Int
    ): List<Repository>? {
        val repository = SearchProvider.provideSearchRepository()

        return repository.getUserRepos(userName, pageNumber)
    }

    suspend fun loadMoreRepositories(
        userName: String,
        pageNumber: Int
    ): List<Repository>? {
        val repository = SearchProvider.provideSearchRepository()

        return repository.getUserRepos(userName, pageNumber)
    }

    suspend fun getLimitRemaining(): ResourceRemote? {
        val loginSearch = SearchProvider.provideSearchRepository()

        return loginSearch.getLimitRemaining()
    }
}
