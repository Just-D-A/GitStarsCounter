package com.example.gitstarscounter.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitstarscounter.entity.UserModel

@Entity(tableName = "Users")
data class UserLocal(
    @PrimaryKey
    override val id: Long,

    override val name: String,

    @ColumnInfo(name = "avatar_url")
    override val avatarUrl: String
): UserModel {
    constructor(user: UserModel) : this (
        id = user.id,
        name = user.name,
        avatarUrl = user.avatarUrl
    )
}