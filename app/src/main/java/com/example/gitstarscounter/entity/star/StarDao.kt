package com.example.gitstarscounter.entity.star

import androidx.room.*
import com.example.gitstarscounter.entity.convectors.DateConverter
import java.util.*


@Dao
@TypeConverters(DateConverter::class)
interface StarDao {
    @Query("SELECT * FROM stars")
    fun getAll(): List<Star>

    @Query("SELECT * FROM stars WHERE starred_at LIKE :starredAt")
    fun loadAllByIds(starredAt: Date?): List<Star>

    @Query("SELECT * FROM stars WHERE repository_id LIKE :repositoryId")
    fun findByRepositoryId(repositoryId: Long): List<Star>

    @Query("SELECT * FROM stars WHERE repository_id LIKE :repositoryId AND user_id LIKE :userId ")
    fun findByRepositoryUserAndId(repositoryId: Long, userId: Long): Star

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg stars: Star)

    @Delete
    fun delete(star: Star)
}

