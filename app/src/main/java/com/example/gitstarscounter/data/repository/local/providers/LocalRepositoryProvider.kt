package com.example.gitstarscounter.data.repository.local.providers

import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.repository.local.entity.LocalRepository
import com.example.gitstarscounter.entity.Repository

class LocalRepositoryProvider {
    private val database = GitStarsApplication.instance.gitStarsCounterComponent.getRoomDatabase()
    private val userTable = database.userDao()
    private val repositoryTable = database.repositoryDao()

    suspend fun getAllRepositories(): List<Repository> {
        val tableRepositoryList = repositoryTable.getAll()
        val repositoryList: MutableList<Repository> = mutableListOf()
        tableRepositoryList.forEach { tableRepository ->
            val user = userTable.getUserById(tableRepository.user.id)
            val repository: Repository = LocalRepository(tableRepository, user)
            repositoryList.add(repository)
        }
        return repositoryList
    }

    suspend fun deleteRepository(repository: Repository) {
        val tableRepository = LocalRepository(repository)
        repositoryTable.delete(tableRepository)
    }
}
