package com.example.gitstarscounter.data.local.database.enitity.star

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gitstarscounter.data.local.database.enitity.repository.TableRepository
import com.example.gitstarscounter.data.local.entity.UserLocal
import com.example.gitstarscounter.data.local.entity.StarLocal

@Entity(
    tableName = "Stars", foreignKeys = [
        ForeignKey(
            entity = TableRepository::class,
            parentColumns = ["id"],
            childColumns = ["repository_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = UserLocal::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class TableStar(
    @PrimaryKey
    @ColumnInfo(name = "starred_at") val starredAt: String,

    @ColumnInfo(name = "repository_id") val repositoryId: Long,

    @ColumnInfo(name = "user_id") val userId: Long
) {
    constructor(star: StarLocal) : this(
        starredAt = star.starredAt.toString(),
        repositoryId = star.repository.id,
        userId = star.user.id
    )
}