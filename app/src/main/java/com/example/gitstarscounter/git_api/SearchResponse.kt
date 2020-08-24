package com.example.gitstarscounter.git_api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "login") val login: String,
    @Json(name = "id") val id: Long,
    @Json(name = "avatar_url")val avatar_url: String
): Serializable

@JsonClass(generateAdapter = true)
data class Repository(
    @Json(name = "name") val name: String,
    @Json(name = "stargazers_count") val allStarsCount: Int
): Serializable

@JsonClass(generateAdapter = true)
data class Star(
    @Json(name = "starred_at") val starred_at: Date,
    @Json(name = "user") val user: User
) : Serializable
