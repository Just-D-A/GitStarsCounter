package com.example.gitstarscounter.data.providers.star

import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star

interface StarProvider {
    suspend fun getRepositoryStars(userName: String, repositoryRemote: Repository, pageNumber: Int): List<Star>?
}
