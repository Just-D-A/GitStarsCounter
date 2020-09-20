package com.example.gitstarscounter.data.to_rename_2.remote.providers

import com.example.gitstarscounter.data.to_rename_2.remote.SearchProvider
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.data.for_providers.star.StarProvider

@Suppress("DEPRECATION", "UNCHECKED_CAST")
class RemoteStarProvider : StarProvider {
    private val searchRepository = SearchProvider.provideSearchStars()

    override suspend fun getRepositoryStars(
        userName: String,
        repositoryRemote: Repository,
        pageNumber: Int
    ): List<Star>? {
        return searchRepository.getStars(userName, repositoryRemote.name, pageNumber)
    }
}
