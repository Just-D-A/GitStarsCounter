package com.example.gitstarscounter.ui.screens.repository

import com.example.gitstarscounter.data.repository.local.providers.LocalRepositoryProvider
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.example.gitstarscounter.ui.screens.stars.StarsActivity
import com.omegar.mvp.InjectViewState
import kotlinx.coroutines.launch

@InjectViewState
class RepositoryPresenter : BasePresenter<RepositoryView>() {
    private val localRepositoryProvider = LocalRepositoryProvider()

    init {
        setRepositoryList()
    }

    private fun setRepositoryList() {
        launchWithWaiting {
            val repositoryList = localRepositoryProvider.getAllRepositories()
            viewState.setRepositoryList(repositoryList)
        }
    }

    fun responseToDeleteRepository(repository: Repository) {
        launch {
            localRepositoryProvider.deleteRepository(repository)
            setRepositoryList()
        }
    }

    fun responseToOpenStars(repository: Repository) {
        StarsActivity.createLauncher(repository.user.name, repository).launch()
    }
}
