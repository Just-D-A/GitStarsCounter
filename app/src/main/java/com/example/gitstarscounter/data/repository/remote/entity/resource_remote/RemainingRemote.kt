package com.example.gitstarscounter.data.repository.remote.entity.resource_remote

import com.squareup.moshi.Json

data class RemainingRemote(
    @Json(name = "remaining")
    val remaining: Int
)
