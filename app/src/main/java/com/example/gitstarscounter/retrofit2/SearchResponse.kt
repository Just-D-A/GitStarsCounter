package com.example.gitstarscounter.retrofit2

import java.util.*

data class User(
    val login: String,
    val id: Long,
    val url: String,
    val html_url: String,
    val followers_url: String,
    val following_url: String,
    val starred_url: String,
    val gists_url: String,
    val type: String
)

data class Star(
    val starred_at: Date,
    val user: User
)
