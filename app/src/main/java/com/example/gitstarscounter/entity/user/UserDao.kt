package com.example.gitstarscounter.entity.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userId)")
    fun getUserById(userId: Long): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}