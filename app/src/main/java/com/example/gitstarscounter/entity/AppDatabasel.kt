package com.example.gitstarscounter.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gitstarscounter.entity.repository.Repository
import com.example.gitstarscounter.entity.repository.RepositoryDao
import com.example.gitstarscounter.entity.star.Star
import com.example.gitstarscounter.entity.star.StarDao
import com.example.gitstarscounter.entity.user.User
import com.example.gitstarscounter.entity.user.UserDao


@Database(entities = [User::class, Repository::class, Star::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao?
    abstract fun repositoryDao(): RepositoryDao?
    abstract fun starDao(): StarDao?
}