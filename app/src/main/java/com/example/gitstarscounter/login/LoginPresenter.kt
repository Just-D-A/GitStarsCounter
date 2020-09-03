package com.example.gitstarscounter.login

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.entity.repository.Repository
import com.example.gitstarscounter.git_api.RepositoryModel

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

    fun openStars(repository: RepositoryModel?) {
        viewState.openStars(repository!!.user.login, repository)
    }

    fun loadMoreRepositories() {

    }

    override fun onLoginResponse(repositoryList: List<RepositoryModel>, noInternetIsVisible: Boolean) {
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