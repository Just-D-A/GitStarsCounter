package com.example.gitstarscounter.data.repository.local.entity.database.star

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gitstarscounter.data.repository.local.entity.database.repository.TableRepository
import com.example.gitstarscounter.data.repository.local.entity.LocalUser
import com.example.gitstarscounter.data.repository.local.entity.LocalStar
import java.util.*

@Entity(
    tableName = "Stars", foreignKeys = [
        ForeignKey(
            entity = TableRepository::class,
            parentColumns = ["id"],
            childColumns = ["repository_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocalUser::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class TableStar(
    @PrimaryKey
    @ColumnInfo(name = "starred_at") val starredAt: Date,

    @ColumnInfo(name = "repository_id") val repositoryId: Long,

    @ColumnInfo(name = "user_id") val userId: Long
) {
    constructor(star: LocalStar) : this(
        starredAt = star.starredAt,
        repositoryId = star.repository.id,
        userId = star.user.id
    )
}