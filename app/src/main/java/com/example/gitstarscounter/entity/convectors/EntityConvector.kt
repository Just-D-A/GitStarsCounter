@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.entity.convectors

import android.annotation.SuppressLint
import com.example.gitstarscounter.entity.repository.Repository
import com.example.gitstarscounter.entity.star.Star
import com.example.gitstarscounter.entity.user.User
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.StarModel
import com.example.gitstarscounter.git_api.UserModel
import com.example.gitstarscounter.stars.StarsEntityProvider

object EntityConvector {

    fun covertUserToEntity(userModel: UserModel): User {
        return User(
            userModel.id,
            userModel.login,
            userModel.avatarUrl
        )
    }

    fun covertRepositoryToEntity(repositoryModel: RepositoryModel): Repository {
        return Repository(
            id = repositoryModel.id,
            name = repositoryModel.name,
            allStarsCount = repositoryModel.allStarsCount,
            userId = repositoryModel.user.id
        )
    }

    fun covertEntityToRepository(
        repository: Repository,
        user: User
    ): RepositoryModel {
        return RepositoryModel(
            id = repository.id,
            name = repository.name.toString(),
            allStarsCount = repository.allStarsCount,
            user = convertEntityToUser(user)
        )
    }


    fun convertEntityToUser(user: User): UserModel {
        return UserModel(user.id, user.name!!, user.avatarUrl)
    }

    @SuppressLint("SimpleDateFormat")
    fun covertEntityToStar(
        star: Star,
        user: User
    ): StarModel {
        return StarModel(StarsEntityProvider.sdf3.parse(star.starredAt), convertEntityToUser(user))

    }

    fun convertStarToEntity(
        starModel: StarModel,
        repositoryId: Long
    ): Star {
        return Star(
            starredAt = starModel.starredAt.toString(),
            repositoryId = repositoryId,
            userId = starModel.user.id
        )
    }
}