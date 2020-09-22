package com.example.gitstarscounter.data.repository.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.entity.User
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Entity(tableName = "Stars")
data class LocalStar(
    @PrimaryKey
    @ColumnInfo(name = "starred_at")
    override val starredAt: Date,
    @Embedded(prefix = "user_")
    override val user: LocalUser,
    @Embedded(prefix = "repository_")
    val repository: LocalRepository
) : Star {
    constructor(tableStar: LocalStar, repository: Repository, user: LocalUser) : this(
        starredAt = tableStar.starredAt,
        user = user,
        repository = LocalRepository(repository)
    )

    constructor(star: Star, repository: Repository) : this(
        starredAt = star.starredAt,
        user = LocalUser(star.user),
        repository = LocalRepository(repository)
    )
}

