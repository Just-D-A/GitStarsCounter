package com.example.gitstarscounter.git_api

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface GithubApiService {
    //https://api.github.com/users/Just-D-A/repos
    @GET("users/{name}/repos")
    fun getUserRepos(
        @Path("name") userName: String,
        @Query("page") pageNumber: Int
    ): Call<List<RepositoryModel?>?>

    //https://api.github.com/repos/Just-D-A/GitStarsCounter/stargazers
    @GET("repos/{userName}/{repositoryName}/stargazers")
    fun getStars(
        @Path("userName") userName: String,
        @Path("repositoryName") repositoryName: String,
        @Header("Accept") param: String,
        @Query("page") pageNumber: Int,
        @Query("per_page") elementsCount: Int
    ): Call<List<StarModel?>?>

    //https://api.github.com/rate_limit
    @GET("https://api.github.com/rate_limit")
    fun getLimitRemaining(): Call<ResourceModel>

    /**
     * Companion object to create the GithubApiService
     */
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
}
