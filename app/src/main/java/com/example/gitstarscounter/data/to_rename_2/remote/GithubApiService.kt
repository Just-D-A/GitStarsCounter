package com.example.gitstarscounter.data.to_rename_2.remote

import com.example.gitstarscounter.data.to_rename_2.remote.entity.RemoteRepository
import com.example.gitstarscounter.data.to_rename_2.remote.entity.RemoteStar
import com.example.gitstarscounter.data.to_rename_2.remote.entity.resource_remote.ResourceRemote
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface GithubApiService {
    companion object Factory {
        fun create(): GithubApiService {
            val moshi: Moshi = Moshi.Builder()
                .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
                .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())//etc
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return retrofit.create(GithubApiService::class.java)
        }
    }

    //https://api.github.com/users/Just-D-A/repos
    @GET("users/{name}/repos")
    suspend fun getUserRepos(
        @Path("name") userName: String,
        @Query("page") pageNumber: Int
    ): List<RemoteRepository>?

    //https://api.github.com/repos/Just-D-A/GitStarsCounter/stargazers
    @GET("repos/{userName}/{repositoryName}/stargazers")
    suspend fun getStars(
        @Path("userName") userName: String,
        @Path("repositoryName") repositoryName: String,
        @Header("Accept") param: String,
        @Query("page") pageNumber: Int,
        @Query("per_page") elementsCount: Int
    ): List<RemoteStar>?

    //https://api.github.com/rate_limit
    @GET("https://api.github.com/rate_limit")
    suspend fun getLimitRemaining(): ResourceRemote
}
