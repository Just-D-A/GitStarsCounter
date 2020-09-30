package com.example.gitstarscounter.di.module

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import com.example.gitstarscounter.service.StarWorker
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class WorkerModule {
    @Provides
    @Singleton
    fun starWorker(constraints: Constraints): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<StarWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
    }

    @Provides
    @Singleton
    fun getConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }
}
