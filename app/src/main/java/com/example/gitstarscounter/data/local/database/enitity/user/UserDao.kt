package com.example.gitstarscounter.data.local.database.enitity.user

import androidx.room.*
import com.example.gitstarscounter.data.local.entity.UserLocal

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<UserLocal>

    @Query("SELECT * FROM users WHERE id IN (:userId)")
    fun getUserById(userId: Long): UserLocal

    @Query("SELECT * FROM users WHERE name LIKE (:userName)")
    fun getUserByName(userName: String): UserLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg userLocals: UserLocal)

    @Delete
    fun delete(userLocal: UserLocal)
}
