package com.example.gitstarscounter.data.remote.entity

import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.entity.UserModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class RepositoryRemote(
    @Json(name = "id")
    override val id: Long,

    @Json(name = "name")
    override val name: String,

    @Json(name = "stargazers_count")
    override val allStarsCount: Int,

    @Json(name = "owner")
    override val user: UserRemote
) : Serializable, RepositoryModel
