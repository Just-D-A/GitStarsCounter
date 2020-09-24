package com.example.gitstarscounter.data.repository.remote.providers

import com.example.gitstarscounter.data.repository.remote.SearchProvider
import com.example.gitstarscounter.data.repository.remote.entity.RemoteStar
import com.example.gitstarscounter.entity.Repository

class RemoteStarWorkerProvider {
    private val searchRepository = SearchProvider().provideSearchStars()

    suspend fun loadStars(userName: String, repositoryRemote: Repository, pageNumber: Int)
            : List<RemoteStar>? {
        return searchRepository.getStars(userName, repositoryRemote.name, pageNumber)
    }
}
