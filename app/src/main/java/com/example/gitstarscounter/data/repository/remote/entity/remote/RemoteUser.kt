package com.example.gitstarscounter.data.repository.remote.entity.remote

import com.example.gitstarscounter.entity.User
import com.omega_r.libs.omegatypes.image.Image
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteUser(
    @Json(name = "id")
    override val id: Long,

    @Json(name = "login")
    override val name: String,

    @Json(name = "avatar_url")
    override val avatar: Image?
) : User
