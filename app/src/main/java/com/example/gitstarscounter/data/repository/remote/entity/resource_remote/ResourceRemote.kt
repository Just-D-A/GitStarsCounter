package com.example.gitstarscounter.data.repository.remote.entity.resource_remote

import com.squareup.moshi.Json

data class ResourceRemote(
    @Json(name = "resources")
    val resources: CoreRemote
)
