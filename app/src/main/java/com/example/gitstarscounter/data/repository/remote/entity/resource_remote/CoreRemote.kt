package com.example.gitstarscounter.data.repository.remote.entity.resource_remote

import com.squareup.moshi.Json

data class CoreRemote(
    @Json(name = "core")
    val core: RemainingRemote
)