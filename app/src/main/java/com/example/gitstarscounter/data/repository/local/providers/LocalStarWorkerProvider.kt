package com.example.gitstarscounter.data.repository.local.providers

import android.util.Log
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.repository.local.entity.LocalRepository
import com.example.gitstarscounter.data.repository.local.entity.LocalStar
import com.example.gitstarscounter.entity.Repository

class LocalStarWorkerProvider {
    companion object {
        private const val TAG = "ServiceProvider"
    }

    private val database = GitStarsApplication.instance.appRoomDatabase
    private val userTable = database.userDao()
    private val repositoryTable = database.repositoryDao()
    private val starTable = database.starDao()

    private val repositoryModelList =
        mutableListOf<Repository>()

    suspend fun getAllDatabaseRepositories(): List<Repository> {
        val repositoryEntityList = repositoryTable.getAll()
        Log.d(TAG, repositoryEntityList.size.toString())
        repositoryEntityList.forEach {
            val user = userTable.getUserById(it.user.id)
            repositoryModelList.add(LocalRepository(it, user))
        }
        return repositoryModelList
    }

    suspend fun findNewStars(
        listFromApiRemoteStar: List<com.example.gitstarscounter.data.repository.remote.entity.RemoteStar>,
        repository: Repository
    ): MutableList<com.example.gitstarscounter.entity.Star> {
        val newStars = mutableListOf<com.example.gitstarscounter.entity.Star>()

        Log.d(TAG, repository.name + " " + listFromApiRemoteStar.size.toString())

        listFromApiRemoteStar.forEach { starFromApi ->
            val starFromDB =
                starTable.findByRepositoryUserAndId(repository.id, starFromApi.user.id)
            if (starFromDB == null) {
                Log.d(TAG, "add NEW star")
                newStars.add(starFromApi)
                starTable.insertAll(
                    LocalStar(starFromApi, repository as LocalRepository)
                )
            }
        }

        return newStars
    }
}
