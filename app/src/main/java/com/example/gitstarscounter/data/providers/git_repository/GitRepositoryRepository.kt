package com.example.gitstarscounter.data.providers.git_repository

import com.example.gitstarscounter.data.repository.local.providers.LocalRepositoryProvider
import com.example.gitstarscounter.entity.Repository

class GitRepositoryRepository {
    private val localRepositoryProvider = LocalRepositoryProvider()

    suspend fun getAllRepositories(): List<Repository> {
        return localRepositoryProvider.getAllRepositories()
    }

    suspend fun deleteRepository(repository: Repository) {
        localRepositoryProvider.deleteRepository(repository)
    }
}
