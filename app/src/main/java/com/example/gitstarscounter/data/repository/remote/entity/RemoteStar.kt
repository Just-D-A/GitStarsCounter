package com.example.gitstarscounter.data.repository.remote.entity

import com.example.gitstarscounter.entity.Star
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class RemoteStar(
    @Json(name = "starred_at")
    override val starredAt: Date,

    @Json(name = "user")
    override val user: RemoteUser
) : Star
