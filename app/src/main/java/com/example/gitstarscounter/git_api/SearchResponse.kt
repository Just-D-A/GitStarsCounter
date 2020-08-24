package com.example.gitstarscounter.git_api

import com.squareup.moshi.Json
import java.util.*
import java.io.Serializable

data class User(
    val login: String,
    val id: Long,
    val avatar_url: String
): Serializable

data class Repository(
    val name: String
)

data class Star(
    val starred_at: Date,
    val user: User
) : Serializable
