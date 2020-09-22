package com.example.gitstarscounter.ui.screens.repository

import android.content.Context
import com.example.gitstarscounter.data.repository.local.providers.LocalRepositoryProvider
import com.example.gitstarscounter.data.repository.remote.entity.RemoteRepository
import com.example.gitstarscounter.data.repository.remote.entity.RemoteUser
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.example.gitstarscounter.ui.screens.stars.StarsActivity
import com.omegar.mvp.InjectViewState
import kotlinx.coroutines.launch

@InjectViewState
class RepositoryPresenter: BasePresenter<RepositoryView>() {
    private val localRepositoryProvider = LocalRepositoryProvider()

    init{
        setRepositoryList()
    }

    private fun setRepositoryList() {
        launch {
            val repositoryList = localRepositoryProvider.getAllRepositories()
            viewState.setRepositoryList(repositoryList!!)
        }
    }

    fun responseToDeleteRepository(repository: Repository) {
        launch {
            localRepositoryProvider.deleteRepository(repository)
            setRepositoryList()
        }
    }

    fun responseToOpenStars(context: Context, repository: Repository) {
        StarsActivity.createLauncher(
            repository.user.name,
            RemoteRepository(
                id = repository!!.id,
                name = repository.name,
                allStarsCount = repository.allStarsCount,
                user = RemoteUser(
                    repository.user.id,
                    repository.user.name,
                    repository.user.avatarUrl
                )
            )
        )
            .launch(context)
    }
}
