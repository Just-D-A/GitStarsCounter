package com.example.gitstarscounter.data.local.entity

import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.local.convectors.DateConverter
import com.example.gitstarscounter.data.local.database.enitity.star.TableStar
import com.example.gitstarscounter.data.remote.entity.StarRemote
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.entity.StarModel
import com.example.gitstarscounter.entity.UserModel
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class StarLocal(
    override val starredAt: Date,
    override val user: UserModel,
    val repository: RepositoryModel
) : StarModel {
    constructor(tableStar: TableStar) : this(
        starredAt = DateConverter.sdf3.parse(tableStar.starredAt),
        user = GitStarsApplication.instance?.database!!.userDao()!!.getUserById(tableStar.userId),
        repository = RepositoryLocal(
            GitStarsApplication.instance?.database!!.repositoryDao()!!
                .getRepositoryById(tableStar.repositoryId)
        )
    )

    constructor(star: StarRemote, repository: RepositoryModel) : this(
        starredAt = star.starredAt,
        user = star.user,
        repository = repository
    )
}

