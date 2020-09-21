package com.example.gitstarscounter.data.repository.local.entity

import com.example.gitstarscounter.data.repository.local.entity.database.repository.TableRepository
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.User

data class LocalRepository(
    override val id: Long,
    override val name: String,
    override val allStarsCount: Int?,
    override val user: User
) : Repository {
    constructor(tableRepository: TableRepository, user: LocalUser) : this(
        id = tableRepository.id,
        name = tableRepository.name!!,
        allStarsCount = tableRepository.allStarsCount,
        user = user
    )
}
