package com.example.gitstarscounter.entity.user

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE id IN (:userId)")
    fun getUserById(userId: Long): User

    @Query("SELECT * FROM users WHERE name LIKE (:userName)")
    fun getUserByName(userName: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}
