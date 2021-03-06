package com.example.gitstarscounter.data.repository.local.providers

import android.util.Log
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.repository.local.entity.LocalStar
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star

class LocalStarWorkerProvider {
    companion object {
        private const val TAG = "ServiceProvider"
    }

    private val database = GitStarsApplication.instance.gitStarsCounterComponent.getRoomDatabase()
    private val repositoryTable = database.repositoryDao()
    private val starTable = database.starDao()
    private val repositoryModelList = mutableListOf<Repository>()

    suspend fun getAllDatabaseRepositories(): List<Repository> {
        val repositoryEntityList = repositoryTable.getAll()
        Log.d(TAG, repositoryEntityList.size.toString())
        repositoryEntityList.forEach {
            repositoryModelList.add(it)
        }
        return repositoryModelList
    }

    suspend fun findNewStars(
        listFromApiRemoteStar: List<Star>,
        repository: Repository
    ): List<Star> {
        val newStars = mutableListOf<Star>()

        Log.d(TAG, repository.name + " " + listFromApiRemoteStar.size.toString())

        listFromApiRemoteStar.forEach { starFromApi ->
            val starFromDB =
                starTable.findByRepositoryUserAndId(repository.id, starFromApi.user.id)
            if (starFromDB == null) {
                Log.d(TAG, "add NEW star")
                newStars.add(starFromApi)
                starTable.insertAll(LocalStar(starFromApi, repository))
            }
        }
        return newStars
    }
}
