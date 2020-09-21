package com.example.gitstarscounter.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gitstarscounter.data.repository.local.convectors.DateConverter
import com.example.gitstarscounter.data.repository.local.entity.database.repository.RepositoryDao
import com.example.gitstarscounter.data.repository.local.entity.database.repository.TableRepository
import com.example.gitstarscounter.data.repository.local.entity.database.star.StarDao
import com.example.gitstarscounter.data.repository.local.entity.database.star.TableStar
import com.example.gitstarscounter.data.repository.local.entity.database.user.UserDao
import com.example.gitstarscounter.data.repository.local.entity.LocalUser

@TypeConverters(DateConverter::class)
@Database(
    entities = [LocalUser::class, TableRepository::class, TableStar::class],
    version = 3,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repositoryDao(): RepositoryDao
    abstract fun starDao(): StarDao
}
