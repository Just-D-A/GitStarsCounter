package com.example.gitstarscounter.entity

import com.omega_r.libs.omegatypes.image.Image
import java.io.Serializable

interface User : Serializable {
    val id: Long
    val name: String
    val avatar: Image?
}
