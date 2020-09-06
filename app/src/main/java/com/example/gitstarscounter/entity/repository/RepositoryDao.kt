package com.example.gitstarscounter.entity.repository

import androidx.room.*

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM repositories")
    fun getAll(): List<Repository>

    @Query("SELECT * FROM repositories WHERE id IN (:repositoryId)")
    fun getRepositoryById(repositoryId: Long): Repository

    @Query("SELECT * FROM repositories WHERE user_id IN (:userId)")
    fun getRepositoriesByUserId(userId: Long): List<Repository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg repositories: Repository)

    @Delete
    fun delete(repository: Repository)
}
