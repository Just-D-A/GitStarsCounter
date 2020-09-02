package com.example.gitstarscounter.login

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.stars.StarsEntityProvider

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@InjectViewState
class LoginPresenter : MvpPresenter<LoginView>(), LoginCallback {

    var isNoInternetVisible: Boolean = false
    val loginProvider = LoginProvider()
 //   lateinit var database: AppDatabase
    lateinit var userName: String


    fun loadUser(userName: String) {
        viewState.startLoading()
        this.userName = userName
        loginProvider.loadUser(userName, this)
    }

    fun openStars(repository: Repository?) {
      /*  val loginEntityProvider = LoginEntityProvider(this)
        loginEntityProvider.insertUserAndRepository(repository!!.user, repository)*/
        viewState.openStars(repository!!.user.login, repository)
    }

    override fun onLoginResponse(repositoryList: List<Repository>, noInternerIsVisible: Boolean) {
        viewState.changeVisibilityOfNoInternetView(noInternerIsVisible)
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