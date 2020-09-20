package com.example.gitstarscounter.data.to_rename_2.local.entity.database.star

import androidx.room.*
import java.util.*

@Dao
interface StarDao {

    @Query("SELECT * FROM stars")
    suspend fun getAll(): List<TableStar>

    @Query("SELECT * FROM stars WHERE starred_at LIKE :starredAt")
    suspend fun loadAllByIds(starredAt: Date?): List<TableStar>

    @Query("SELECT * FROM stars WHERE repository_id LIKE :repositoryId")
    suspend fun findByRepositoryId(repositoryId: Long): List<TableStar>

    @Query("SELECT * FROM stars WHERE repository_id LIKE :repositoryId AND user_id LIKE :userId ")
    suspend fun findByRepositoryUserAndId(repositoryId: Long, userId: Long): TableStar?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg tableStarLocal: TableStar)

    @Delete
    suspend fun delete(tableStarLocal: TableStar)
}
