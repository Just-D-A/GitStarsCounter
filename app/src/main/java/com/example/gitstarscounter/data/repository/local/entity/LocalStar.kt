package com.example.gitstarscounter.data.repository.local.entity

import com.example.gitstarscounter.data.repository.local.entity.database.star.TableStar
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.entity.User
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class LocalStar(
    override val starredAt: Date,
    override val user: User,
    val repository: Repository
) : Star {
    constructor(tableStar: TableStar, repository: Repository, user: User) : this(
        starredAt = tableStar.starredAt,
        user = user,
        repository = repository
    )

    constructor(star: Star, repository: Repository) : this(
        starredAt = star.starredAt,
        user = star.user,
        repository = repository
    )
}

