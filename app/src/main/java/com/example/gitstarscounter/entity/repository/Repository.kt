package com.example.gitstarscounter.entity.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitstarscounter.entity.user.User

@Entity
data class Repository(
    @PrimaryKey val id: Int,
    val name: String?,
    @ColumnInfo(name = "user_id") val user_id: Long
)

