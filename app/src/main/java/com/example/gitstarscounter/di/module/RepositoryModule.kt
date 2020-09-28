package com.example.gitstarscounter.di.module

import com.example.gitstarscounter.data.providers.login.LoginRepository
import com.example.gitstarscounter.data.providers.star.StarRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    fun providesLoginRepository(): LoginRepository {
        return LoginRepository()
    }

    @Provides
    fun providesStarRepository(): StarRepository {
        return StarRepository()
    }
}