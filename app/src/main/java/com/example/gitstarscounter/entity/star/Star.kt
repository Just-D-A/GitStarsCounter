package com.example.gitstarscounter.entity.star

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gitstarscounter.entity.repository.Repository
import com.example.gitstarscounter.entity.user.User

@Entity(
    tableName = "Stars", foreignKeys = [
        ForeignKey(
            entity = Repository::class,
            parentColumns = ["id"],
            childColumns = ["repository_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class Star(
    @PrimaryKey
    @ColumnInfo(name = "starred_at") val starredAt: String,
    @ColumnInfo(name = "repository_id") val repositoryId: Long,
    @ColumnInfo(name = "user_id") val userId: Long
)
