package com.example.gitstarscounter.entity

import java.io.Serializable
import java.util.*

interface Star : Serializable {
    val starredAt: Date
    val user: User
}
