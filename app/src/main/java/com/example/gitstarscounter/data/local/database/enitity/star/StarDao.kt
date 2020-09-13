package com.example.gitstarscounter.data.local.database.enitity.star

import androidx.room.*
import com.example.gitstarscounter.data.local.convectors.DateConverter
import java.util.*

@Dao
@TypeConverters(DateConverter::class)
interface StarDao {

    @Query("SELECT * FROM stars")
    fun getAll(): List<TableStar>

    @Query("SELECT * FROM stars WHERE starred_at LIKE :starredAt")
    fun loadAllByIds(starredAt: Date?): List<TableStar>

    @Query("SELECT * FROM stars WHERE repository_id LIKE :repositoryId")
    fun findByRepositoryId(repositoryId: Long): List<TableStar>

    @Query("SELECT * FROM stars WHERE repository_id LIKE :repositoryId AND user_id LIKE :userId ")
    fun findByRepositoryUserAndId(repositoryId: Long, userId: Long): TableStar

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tableStarLocal: TableStar)

    @Delete
    fun delete(tableStarLocal: TableStar)
}
