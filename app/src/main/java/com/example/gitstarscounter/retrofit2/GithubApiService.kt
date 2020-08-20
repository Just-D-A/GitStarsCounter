package com.example.gitstarscounter.retrofit2

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GithubApiService {

    @GET("users/{name}")
    fun getUser(@Path("name") userName: String): Observable<User?>?

    //https://api.github.com/repos/Just-D-A/GitStarsCounter/stargazers
    @GET("repos/{name}/GitStarsCounter/stargazers")
    fun getStars(
        @Path("name") userName: String,
        @Header("Accept") param: String
    ): Observable<List<Star?>?>?

    @GET("user")
    fun getUserDetails()

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): GithubApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()

            return retrofit.create(GithubApiService::class.java);
        }
    }
}