package com.example.gitstarscounter.data.for_providers.login

import com.example.gitstarscounter.entity.Repository

interface LoginProvider {
    suspend fun getUsersRepositories(userName: String, pageNumber: Int): List<Repository>?
}
