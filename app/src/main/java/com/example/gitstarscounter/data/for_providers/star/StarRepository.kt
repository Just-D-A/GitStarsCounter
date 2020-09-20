package com.example.gitstarscounter.data.for_providers.star

import com.example.gitstarscounter.data.to_rename_2.local.providers.LocalStarProvider
import com.example.gitstarscounter.data.to_rename_2.remote.RequestLimit
import com.example.gitstarscounter.data.to_rename_2.remote.entity.RemoteRepository
import com.example.gitstarscounter.data.to_rename_2.remote.providers.RemoteStarProvider
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star
import java.io.IOException

open class StarRepository: StarProvider {
    companion object {
        const val TAG = "StarRepository"
    }

    private val remoteStarProvider = RemoteStarProvider()
    private val localStarProvider = LocalStarProvider()

    override suspend fun getRepositoryStars(
        userName: String,
        repositoryRemote: Repository,
        pageNumber: Int
    ): List<Star>? {
        return try {
            if(!RequestLimit.hasRequest()) {
                throw IOException()
            }
            val starsList = remoteStarProvider.getRepositoryStars(userName,repositoryRemote,pageNumber)
            updateDatabase(starsList, repositoryRemote as RemoteRepository)
            starsList
        } catch(noDataException: IOException) {
            localStarProvider.getRepositoryStars(userName,repositoryRemote,pageNumber)
        }
    }

    private suspend fun updateDatabase(starsList: List<Star>?, repository: RemoteRepository) {
        localStarProvider.insertToDatabase(starsList as List<Star>, repository)
        localStarProvider.checkUnstar(starsList, repository)
    }
}
