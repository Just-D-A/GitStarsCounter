package com.example.gitstarscounter.data.providers.star

import android.util.Log
import com.example.gitstarscounter.LimiteException
import com.example.gitstarscounter.data.repository.local.providers.LocalStarProvider
import com.example.gitstarscounter.data.repository.remote.RequestLimit
import com.example.gitstarscounter.data.repository.remote.providers.RemoteStarProvider
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.ui.screens.stars.SearchStars
import java.io.IOException

open class StarRepository {
    companion object {
        const val TAG = "StarRepository"
    }

    private val remoteStarProvider = RemoteStarProvider()
    private val localStarProvider = LocalStarProvider()
    suspend fun getRepositoryStars(
        userName: String,
        repositoryRemote: Repository
    ): List<Star> {
        val starsList = mutableListOf<Star>()
        var pageNumber = 1
        return try {
            if (!RequestLimit.hasRequest()) {
                throw LimiteException()
            }
            do {
                val starPackage =
                    remoteStarProvider.getRepositoryStars(userName, repositoryRemote, pageNumber)
                starsList.addAll(starPackage)
                pageNumber++
            } while (starPackage.size == SearchStars.MAX_ELEMENTS_FROM_API)

            updateDatabase(starsList, repositoryRemote)
            starsList
        } catch (e: Exception) {
            localStarProvider.getRepositoryStars(userName, repositoryRemote, pageNumber)
        }
    }

    private suspend fun updateDatabase(starsList: List<Star>, repository: Repository) {
        Log.d(TAG, "SIZE: ${starsList.size}")
        localStarProvider.insertToDatabase(starsList, repository)
        localStarProvider.checkUnstar(starsList, repository)
    }
}
