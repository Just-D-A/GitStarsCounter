package com.example.gitstarscounter.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @PrimaryKey val id: Long,
    val name: String?,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String
)