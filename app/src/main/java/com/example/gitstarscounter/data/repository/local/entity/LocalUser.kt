package com.example.gitstarscounter.data.repository.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitstarscounter.entity.User
import com.omega_r.libs.omegatypes.image.Image

@Entity(tableName = "Users")
data class LocalUser(
    @PrimaryKey
    override val id: Long,

    override val name: String,

    @ColumnInfo(name = "avatar_url")
    override val avatar: Image?
) : User {
    constructor(user: User) : this(
        id = user.id,
        name = user.name,
        avatar = user.avatar
    )
}
