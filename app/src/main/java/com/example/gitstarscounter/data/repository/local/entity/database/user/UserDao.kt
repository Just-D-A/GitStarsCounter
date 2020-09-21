package com.example.gitstarscounter.data.repository.local.entity.database.user

import androidx.room.*
import com.example.gitstarscounter.data.repository.local.entity.LocalUser

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<LocalUser>

    @Query("SELECT * FROM users WHERE id IN (:userId)")
    suspend fun getUserById(userId: Long): LocalUser

    @Query("SELECT * FROM users WHERE name LIKE (:userName)")
    suspend fun getUserByName(userName: String): LocalUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg localUsers: LocalUser)

    @Delete
    suspend fun delete(localUser: LocalUser)
}
