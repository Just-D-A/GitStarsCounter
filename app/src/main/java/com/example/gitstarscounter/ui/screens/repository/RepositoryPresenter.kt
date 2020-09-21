package com.example.gitstarscounter.ui.screens.repository

import com.example.gitstarscounter.data.repository.local.providers.LocalRepositoryProvider
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BasePresenter
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
}
