package com.example.gitstarscounter.retrofit2

import io.reactivex.Observable

class SearchRepository(val apiService: GithubApiService) {
    fun getUser(userName: String): Observable<User?>? {
        return apiService.getUser(userName)
    }
}