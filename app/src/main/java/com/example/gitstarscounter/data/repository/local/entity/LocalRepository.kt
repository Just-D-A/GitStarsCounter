package com.example.gitstarscounter.data.repository.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitstarscounter.entity.Repository

@Entity(tableName = "Repositories")
data class LocalRepository(
    @PrimaryKey
    override val id: Long,
    override val name: String,
    @ColumnInfo(name = "all_stars_counter")
    override val allStarsCount: Int?,
    @Embedded(prefix = "user_")
    override val user: LocalUser
) : Repository {
    constructor(repository: LocalRepository, user: LocalUser) : this(
        id = repository.id,
        name = repository.name,
        allStarsCount = repository.allStarsCount,
        user = user
    )

    constructor(repository: Repository) : this(
        id = repository.id,
        name = repository.name,
        allStarsCount = repository.allStarsCount,
        user = LocalUser(repository.user)
    )
}

