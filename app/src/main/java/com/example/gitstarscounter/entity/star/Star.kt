package com.example.gitstarscounter.entity.star

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Star(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "starred_at") val starredAt: Date,
    @ColumnInfo(name = "repository_id") val repositoryId: Long
)