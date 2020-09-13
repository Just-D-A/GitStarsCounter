package com.example.gitstarscounter.data.local.database.enitity.repository

import androidx.room.*

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM repositories")
    fun getAll(): List<TableRepository>

    @Query("SELECT * FROM repositories WHERE id IN (:repositoryId)")
    fun getRepositoryById(repositoryId: Long): TableRepository

    @Query("SELECT * FROM repositories WHERE user_id IN (:userId)")
    fun getRepositoriesByUserId(userId: Long): List<TableRepository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tableRepositories: TableRepository)

    @Delete
    fun delete(TableRepository: TableRepository)
}
