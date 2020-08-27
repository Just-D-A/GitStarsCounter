package com.example.gitstarscounter.entity.star

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gitstarscounter.entity.star.Star

interface StarDao {
    @Query("SELECT * FROM star")
    fun getAll(): List<Star>

    @Query("SELECT * FROM star WHERE id IN (:starIds)")
    fun loadAllByIds(starIds: IntArray): List<Star>

    @Query("SELECT * FROM star WHERE repository_id LIKE :repositoryId")
    //AND " + //"last_name LIKE :last LIMIT 1")
    fun findByRepositoryId(repositoryId: Long): List<Star>

    @Insert
    fun insertAll(vararg stars: Star)

    @Delete
    fun delete(star: Star)
}