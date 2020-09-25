package com.example.gitstarscounter.data.repository.remote

import com.example.gitstarscounter.data.repository.remote.entity.remote.RemoteRepository
import com.example.gitstarscounter.data.repository.remote.entity.remote.RemoteStar
import com.example.gitstarscounter.data.repository.remote.entity.resource_remote.ResourceRemote
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    //https://api.github.com/users/Just-D-A/repos
    @GET("users/{name}/repos")
    suspend fun getUserRepos(
        @Path("name") userName: String,
        @Query("page") pageNumber: Int
    ): List<RemoteRepository>

    //https://api.github.com/repos/Just-D-A/GitStarsCounter/stargazers
    @GET("repos/{userName}/{repositoryName}/stargazers")
    suspend fun getStars(
        @Path("userName") userName: String,
        @Path("repositoryName") repositoryName: String,
        @Header("Accept") param: String,
        @Query("page") pageNumber: Int,
        @Query("per_page") elementsCount: Int
    ): List<RemoteStar>

    //https://api.github.com/rate_limit
    @GET("https://api.github.com/rate_limit")
    suspend fun getLimitRemaining(): ResourceRemote
}
