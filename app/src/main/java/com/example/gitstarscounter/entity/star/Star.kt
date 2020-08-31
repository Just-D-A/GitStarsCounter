package com.example.gitstarscounter.entity.star

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gitstarscounter.entity.repository.Repository
import java.sql.Date

@Entity(tableName = "Stars", foreignKeys = [
    ForeignKey(entity = Repository::class,
        parentColumns = ["id"],
        childColumns = ["repository_id"],
        onDelete = ForeignKey.CASCADE
    )])
data class Star(
    @PrimaryKey
    @ColumnInfo(name = "starred_at") val starredAt: Long,
    @ColumnInfo(name = "repository_id") val repositoryId: Long
)