package com.example.gitstarscounter.entity.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gitstarscounter.entity.user.User

@Entity(
    tableName = "Repositories", foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.NO_ACTION
        )]
)
data class Repository(
    @PrimaryKey
    val id: Long,

    val name: String?,

    @ColumnInfo(name = "all_stars_count")
    val allStarsCount: Int,

    @ColumnInfo(name = "user_id")
    val userId: Long
)
