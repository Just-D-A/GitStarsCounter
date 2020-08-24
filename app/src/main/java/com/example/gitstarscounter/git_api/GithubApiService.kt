package com.example.gitstarscounter.git_api

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.lang.reflect.Type
import java.time.LocalDateTime

interface GithubApiService {

    //https://api.github.com/users/Just-D-A/repos
    @GET("users/{name}/repos")
    fun getUserRepos(@Path("name") userName: String): Call<List<Repository?>?>

    //https://api.github.com/repos/Just-D-A/GitStarsCounter/stargazers
    @GET("repos/{userName}/{repositoryName}/stargazers")
    fun getStars(
        @Path("userName") userName: String,
        @Path("repositoryName") repositoryName: String,
        @Header("Accept") param: String
    ): Call<List<Star?>?>

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): GithubApiService {
            val moshi: Moshi = Moshi.Builder()
                .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
              //  .add(Star::class.java, StarJsonAdapter(Moshi.Builder().build()))
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return retrofit.create(GithubApiService::class.java)
        }
    }
}