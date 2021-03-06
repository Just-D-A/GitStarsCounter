package com.example.gitstarscounter.data.providers.login

import com.example.gitstarscounter.entity.Repository

interface LoginProvider {
    suspend fun getUsersRepositories(userName: String, pageNumber: Int): List<Repository>?
}
