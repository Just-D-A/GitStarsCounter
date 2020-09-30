package com.example.gitstarscounter.ui.screens.repository

import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.providers.git_repository.GitRepositoryRepository
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.example.gitstarscounter.ui.screens.stars.StarsActivity
import com.omegar.mvp.InjectViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@InjectViewState
class RepositoryPresenter : BasePresenter<RepositoryView>() {
    @Inject
    lateinit var gitRepositoryRepository: GitRepositoryRepository

    init {
        setRepositoryList()
        GitStarsApplication.instance.gitStarsCounterComponent.inject(this)
    }

    private fun setRepositoryList() {
        launchWithWaiting {
            val repositoryList = gitRepositoryRepository.getAllRepositories()
            viewState.setRepositoryList(repositoryList)
        }
    }

    fun requestToDeleteRepository(repository: Repository) {
        launch {
            gitRepositoryRepository.deleteRepository(repository)
            setRepositoryList()
        }
    }

    fun requestToOpenStars(repository: Repository) {
        StarsActivity.createLauncher(repository.user.name, repository).launch()
    }
}
