package com.example.gitstarscounter.di.module

import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import com.example.gitstarscounter.service.RateLimitWorker
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
    fun limitWorker(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<RateLimitWorker>(1, TimeUnit.HOURS, 30, TimeUnit.HOURS)
            .build()
    }

    @Provides
    @Singleton
    @Named("StarWorker")
    fun starWorker(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<StarWorker>(1, TimeUnit.HOURS, 30, TimeUnit.HOURS)
            .build()
    }
}
