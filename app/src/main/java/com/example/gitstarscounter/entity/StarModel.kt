package com.example.gitstarscounter.entity

import java.io.Serializable
import java.util.*

interface StarModel : Serializable {
    val starredAt: Date
    val user: UserModel
}
