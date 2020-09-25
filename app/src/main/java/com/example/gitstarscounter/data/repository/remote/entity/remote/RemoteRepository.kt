package com.example.gitstarscounter.data.repository.remote.entity.remote

import com.example.gitstarscounter.entity.Repository
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteRepository(
    @Json(name = "id")
    override val id: Long,

    @Json(name = "name")
    override val name: String,

    @Json(name = "stargazers_count")
    override val allStarsCount: Int?,

    @Json(name = "owner")
    override val user: RemoteUser
) : Repository
