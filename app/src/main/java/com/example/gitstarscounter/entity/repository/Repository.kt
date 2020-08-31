package com.example.gitstarscounter.entity.repository

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.gitstarscounter.entity.user.User

@Entity(
    tableName = "Repositories", foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = CASCADE
        )]
)
data class Repository(
    @PrimaryKey
    val id: Long,

    val name: String?,

    @ColumnInfo(name = "user_id")
    val userId: Long
)

