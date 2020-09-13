package com.example.gitstarscounter.data.remote.entity

import com.example.gitstarscounter.entity.StarModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.util.*

@JsonClass(generateAdapter = true)
data class StarRemote(
    @Json(name = "starred_at")
    override val starredAt: Date,

    @Json(name = "user")
    override val user: UserRemote
) : StarModel
