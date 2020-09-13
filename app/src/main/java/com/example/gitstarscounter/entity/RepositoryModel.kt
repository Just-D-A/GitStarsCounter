package com.example.gitstarscounter.entity

import java.io.Serializable

interface RepositoryModel : Serializable {
    val id: Long
    val name: String
    val allStarsCount: Int
    val user: UserModel
}
