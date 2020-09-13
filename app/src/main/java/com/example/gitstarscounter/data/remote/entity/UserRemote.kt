package com.example.gitstarscounter.data.remote.entity

import com.example.gitstarscounter.entity.UserModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class UserRemote(
    @Json(name = "id")
    override val id: Long,

    @Json(name = "login")
    override val name: String,

    @Json(name = "avatar_url")
    override val avatarUrl: String
) : Serializable, UserModel
