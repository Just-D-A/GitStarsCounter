package com.example.gitstarscounter.login

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Repository

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@InjectViewState
class LoginPresenter : MvpPresenter<LoginView>(), LoginCallback {

    val loginProvider = LoginProvider()

    fun loadUser(userName: String) {
        viewState.startLoading()

        loginProvider.loadUser(userName, this)
    }

    fun openStars(userName: String, repository: Repository?) {
        if (repository != null) {
            viewState.openStars(userName, repository)
        }
    }

    override fun onLoginResponse(repositoryList: List<Repository>) {

            viewState.setupRepositoriesList(repositoryList)
        viewState.endLoading()
    }

    override fun onError(textResource: Int) {
        viewState.endLoading()
        viewState.showError(textResource)
    }
}