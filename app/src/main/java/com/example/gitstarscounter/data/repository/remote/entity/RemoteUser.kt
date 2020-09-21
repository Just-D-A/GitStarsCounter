package com.example.gitstarscounter.data.repository.remote.entity

import com.example.gitstarscounter.entity.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteUser(
    @Json(name = "id")
    override val id: Long,

    @Json(name = "login")
    override val name: String,

    @Json(name = "avatar_url")
    override val avatarUrl: String?
) : User
