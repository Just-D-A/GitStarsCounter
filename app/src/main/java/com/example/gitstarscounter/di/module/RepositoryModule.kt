package com.example.gitstarscounter.di.module

import com.example.gitstarscounter.data.providers.git_repository.GitRepositoryRepository
import com.example.gitstarscounter.data.providers.login.LoginRepository
import com.example.gitstarscounter.data.providers.star.StarRepository
import com.example.gitstarscounter.data.providers.worker.WorkerRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun providesLoginRepository(): LoginRepository {
        return LoginRepository()
    }

    @Singleton
    @Provides
    fun providesStarRepository(): StarRepository {
        return StarRepository()
    }

    @Singleton
    @Provides
    fun providesStarWorkerRepository(): WorkerRepository {
        return WorkerRepository()
    }

    @Singleton
    @Provides
    fun providesGitRepositoryRepository(): GitRepositoryRepository {
        return GitRepositoryRepository()
    }
}