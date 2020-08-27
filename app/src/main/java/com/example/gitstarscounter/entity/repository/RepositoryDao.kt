package com.example.gitstarscounter.entity.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

interface RepositoryDao {
    @Query("SELECT * FROM repository")
    fun getAll(): List<Repository>

    @Query("SELECT * FROM repository WHERE id IN (:repositoryId)")
    fun getRepositoryById(repositoryId: Long): Repository

    @Insert
    fun insertAll(vararg repositories: Repository)

    @Delete
    fun delete(repository: Repository)
}