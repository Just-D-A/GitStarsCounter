package com.example.gitstarscounter.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gitstarscounter.data.repository.local.convectors.DateConverter
import com.example.gitstarscounter.data.repository.local.convectors.ImageConverter
import com.example.gitstarscounter.data.repository.local.entity.LocalRepository
import com.example.gitstarscounter.data.repository.local.entity.LocalStar
import com.example.gitstarscounter.data.repository.local.dao.RepositoryDao
import com.example.gitstarscounter.data.repository.local.dao.StarDao
import com.example.gitstarscounter.data.repository.local.dao.UserDao
import com.example.gitstarscounter.data.repository.local.entity.LocalUser

@TypeConverters(DateConverter::class, ImageConverter::class)
@Database(
    entities = [LocalUser::class, LocalRepository::class, LocalStar::class],
    version = 3,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repositoryDao(): RepositoryDao
    abstract fun starDao(): StarDao
}
