package com.example.gitstarscounter.data.repository.remote.providers

import com.example.gitstarscounter.data.providers.login.LoginProvider
import com.example.gitstarscounter.data.repository.remote.SearchProvider
import com.example.gitstarscounter.entity.Repository

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST")
class RemoteLoginProvider : LoginProvider {
    companion object {
        private const val TAG = "REMOTE_LOGIN_PROVIDER"
    }

    private val repository = SearchProvider()

    override suspend fun getUsersRepositories(userName: String, pageNumber: Int): List<Repository> {
        return repository.provideSearchRepository().getUserRepos(userName, pageNumber)
    }
}
