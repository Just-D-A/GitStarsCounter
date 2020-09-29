package com.example.gitstarscounter.data.providers.worker

import android.util.Log
import com.example.gitstarscounter.data.providers.star.StarsSearch
import com.example.gitstarscounter.data.repository.local.providers.LocalStarWorkerProvider
import com.example.gitstarscounter.data.repository.remote.RequestLimit
import com.example.gitstarscounter.data.repository.remote.entity.resource_remote.ResourceRemote
import com.example.gitstarscounter.data.repository.remote.exception.LimitException
import com.example.gitstarscounter.data.repository.remote.providers.RemoteStarWorkerProvider
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star

class WorkerRepository {
    companion object {
        private const val TAG = "WorkerRepository"
    }

    private val localWorkerProvider = LocalStarWorkerProvider()
    private val remoteWorkerProvider = RemoteStarWorkerProvider()


    suspend fun getAllDatabaseRepositories(): List<Repository> {
        return localWorkerProvider.getAllDatabaseRepositories()
    }

    suspend fun loadStars(name: String, repositoryRemote: Repository): List<Star> {
        var starsList = emptyList<Star>()
        var pageNumber = 1
        try {
            do {
                if (!RequestLimit.hasRequest()) {
                    throw LimitException()
                }

                val starPackage =
                    remoteWorkerProvider.loadStars(name, repositoryRemote, pageNumber)
                starsList += starPackage
                pageNumber++
            } while (starPackage.size == StarsSearch.MAX_ELEMENTS_FROM_API)
        } catch (e: LimitException) {
            Log.d(TAG, "LimitException")
        }
        return starsList
    }

    suspend fun findNewStars(listRemoteStar: List<Star>, repository: Repository): List<Star> {
        return localWorkerProvider.findNewStars(listRemoteStar, repository)
    }

    suspend fun getLimitRemaining(): ResourceRemote {
        return remoteWorkerProvider.getLimitRemaining()
    }
}