package com.example.gitstarscounter.data.remote.entity

import com.squareup.moshi.Json


data class ResourceRemote(
    @Json(name = "resources")
    val resources: CoreRemote
)

data class CoreRemote(
    @Json(name = "core")
    val core: RemainingRemote
)

data class RemainingRemote(
    @Json(name = "remaining")
    val remaining: Int
)
