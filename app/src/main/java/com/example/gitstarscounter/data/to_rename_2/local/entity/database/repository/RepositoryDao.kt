package com.example.gitstarscounter.data.to_rename_2.local.entity.database.repository

import androidx.room.*

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM repositories")
    suspend fun getAll(): List<TableRepository>

    @Query("SELECT * FROM repositories WHERE id IN (:repositoryId)")
    fun getRepositoryById(repositoryId: Long): TableRepository

    @Query("SELECT * FROM repositories WHERE user_id IN (:userId)")
    suspend fun getRepositoriesByUserId(userId: Long): List<TableRepository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg tableRepositories: TableRepository)

    @Delete
    suspend fun delete(TableRepository: TableRepository)
}
