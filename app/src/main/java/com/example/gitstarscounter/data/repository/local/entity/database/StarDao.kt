package com.example.gitstarscounter.data.repository.local.entity.database

import androidx.room.*
import com.example.gitstarscounter.data.repository.local.entity.LocalStar
import com.example.gitstarscounter.data.repository.local.entity.LocalUser
import java.util.*

@Dao
interface StarDao {

    @Query("SELECT * FROM stars")
    suspend fun getAll(): List<LocalStar>

    @Query("SELECT * FROM stars WHERE repository_id LIKE :repositoryId")
    suspend fun findByRepositoryId(repositoryId: Long): List<LocalStar>

    @Query("SELECT * FROM stars WHERE repository_id LIKE :repositoryId AND user_id LIKE :userId ")
    suspend fun findByRepositoryUserAndId(repositoryId: Long, userId: Long): LocalStar?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg tableStarLocal: LocalStar)

    @Delete
    suspend fun delete(tableStarLocal: LocalStar)
}
