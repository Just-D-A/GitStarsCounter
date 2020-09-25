package com.example.gitstarscounter.data.repository.local.dao

import androidx.room.*
import com.example.gitstarscounter.data.repository.local.entity.LocalRepository

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM repositories")
    suspend fun getAll(): List<LocalRepository>

    @Query("SELECT * FROM repositories WHERE id IN (:repositoryId)")
    fun getRepositoryById(repositoryId: Long): LocalRepository

    @Query("SELECT * FROM repositories WHERE user_id IN (:userId)")
    suspend fun getRepositoriesByUserId(userId: Long): List<LocalRepository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg localRepositories: LocalRepository)

    @Delete
    suspend fun delete(localRepository: LocalRepository)
}
