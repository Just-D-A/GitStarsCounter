package com.example.gitstarscounter.login

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

import com.example.gitstarscounter.git_api.Repository

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@InjectViewState
class LoginPresenter : MvpPresenter<LoginView>(), LoginCallback {

    val loginProvider = LoginProvider()
    lateinit var userName: String


    fun loadUser(userName: String) {
        viewState.startLoading()
        this.userName = userName
        loginProvider.loadUser(userName, this)
    }

    fun openStars(repository: Repository?) {
        viewState.openStars(repository!!.user.login, repository)
    }

    override fun onLoginResponse(repositoryList: List<Repository>, noInternetIsVisible: Boolean) {
        viewState.changeVisibilityOfNoInternetView(noInternetIsVisible)
        viewState.setupRepositoriesList(repositoryList)
        viewState.endLoading()
    }

    override fun onError(textResource: Int) {
        viewState.endLoading()
        viewState.changeVisibilityOfNoInternetView(false)
        val loginEntityProvider = LoginEntityProvider(this)
        loginEntityProvider.getUsersRepositories(userName)

    }
}