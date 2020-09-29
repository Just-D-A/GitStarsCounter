package com.example.gitstarscounter.di.module

import androidx.work.Worker
import com.example.gitstarscounter.data.providers.git_repository.GitRepositoryRepository
import com.example.gitstarscounter.data.providers.login.LoginRepository
import com.example.gitstarscounter.data.providers.star.StarRepository
import com.example.gitstarscounter.data.providers.worker.WorkerRepository
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

    @Provides
    fun providesStarWorkerRepository(): WorkerRepository {
        return WorkerRepository()
    }

    @Provides
    fun providesGitRepositoryRepository(): GitRepositoryRepository {
        return GitRepositoryRepository()
    }
}