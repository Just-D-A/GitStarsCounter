package com.example.gitstarscounter.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gitstarscounter.data.local.database.enitity.repository.RepositoryDao
import com.example.gitstarscounter.data.local.database.enitity.repository.TableRepository
import com.example.gitstarscounter.data.local.database.enitity.star.StarDao
import com.example.gitstarscounter.data.local.database.enitity.star.TableStar
import com.example.gitstarscounter.data.local.database.enitity.user.UserDao
import com.example.gitstarscounter.data.local.entity.UserLocal

@Database(
    entities = [UserLocal::class, TableRepository::class, TableStar::class],
    version = 3,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao?
    abstract fun repositoryDao(): RepositoryDao?
    abstract fun starDao(): StarDao?
}
