package com.example.gitstarscounter.di.module

import android.app.Application
import androidx.room.Room
import com.example.gitstarscounter.data.repository.local.AppRoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(private val application: Application?) {
    @Singleton
    @Provides
    fun providesRoomDatabase(): AppRoomDatabase {
        return Room.databaseBuilder(application!!, AppRoomDatabase::class.java, "database").build()
    }
}
