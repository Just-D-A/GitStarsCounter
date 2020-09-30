package com.example.gitstarscounter.di.module

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import com.example.gitstarscounter.service.LimitWorker
import com.example.gitstarscounter.service.StarWorker
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class WorkerModule {
    @Provides
    @Singleton
    @Named("LimitWorker")
    fun limitWorker(constraints: Constraints): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<LimitWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
    }

    @Provides
    @Singleton
    @Named("StarWorker")
    fun starWorker(constraints: Constraints): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<StarWorker>(1, TimeUnit.HOURS, 15, TimeUnit.MINUTES)
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
