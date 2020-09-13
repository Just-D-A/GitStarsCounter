package com.example.gitstarscounter.data.local.entity

import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.local.database.enitity.repository.TableRepository
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.entity.UserModel

data class RepositoryLocal(
    override val id: Long,
    override val name: String,
    override val allStarsCount: Int,
    override val user: UserModel
) : RepositoryModel {
    constructor(tableRepository: TableRepository) : this(
        id = tableRepository.id,
        name = tableRepository.name!!,
        allStarsCount = tableRepository.allStarsCount,
        user = GitStarsApplication.instance?.database!!.userDao()!!
            .getUserById(tableRepository.userId)
    )
}
