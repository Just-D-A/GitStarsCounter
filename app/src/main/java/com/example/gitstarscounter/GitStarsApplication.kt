package com.example.gitstarscounter

import android.app.Application
import androidx.room.Room
import com.example.gitstarscounter.data.local.database.Database

class GitStarsApplication : Application() {
    var database: Database? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, Database::class.java, "database")
            .build()
    }

    companion object {
        var instance: GitStarsApplication? = null
    }
}
