package com.example.gitstarscounter.data.repository.remote.providers

import com.example.gitstarscounter.data.repository.remote.SearchProvider
import com.example.gitstarscounter.data.repository.remote.entity.remote.RemoteStar
import com.example.gitstarscounter.data.repository.remote.entity.resource_remote.ResourceRemote
import com.example.gitstarscounter.entity.Repository

class RemoteStarWorkerProvider {
    private val searchStarsRepository = SearchProvider().provideSearchStars()
    private val searchRepository = SearchProvider().provideSearchRepository()

    suspend fun loadStars(userName: String, repositoryRemote: Repository, pageNumber: Int)
            : List<RemoteStar> {
        return searchStarsRepository.getStars(userName, repositoryRemote.name, pageNumber)
    }

    suspend fun getLimitRemaining(): ResourceRemote {
        return searchRepository.getLimitRemaining()
    }
}
