package com.example.gitstarscounter.data.to_rename_2.remote.providers

import com.example.gitstarscounter.data.to_rename_2.remote.SearchProvider
import com.example.gitstarscounter.data.to_rename_2.remote.entity.RemoteStar
import com.example.gitstarscounter.entity.Repository

class RemoteStarWorkerProvider {
    private val searchRepository = SearchProvider.provideSearchStars()

    suspend fun loadStars(
        userName: String,
        repositoryRemote: Repository,
        pageNumber: Int
    ): List<RemoteStar>? {
        return searchRepository.getStars(userName, repositoryRemote.name, pageNumber)
    }
}
