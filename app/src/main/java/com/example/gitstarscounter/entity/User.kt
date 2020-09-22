package com.example.gitstarscounter.entity

import java.io.Serializable

interface User : Serializable {
    val id: Long
    val name: String
    val avatarUrl: String?
}