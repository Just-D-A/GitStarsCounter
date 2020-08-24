package com.example.gitstarscounter.login

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Repository

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@InjectViewState
class LoginPresenter : MvpPresenter<LoginView>() {

    fun loadUser(userName: String) {
        viewState.startLoading()
        LoginProvider(this).loadUser(userName)
    }

    fun getRepositories(repositoriesList: List<Repository?>?) {
        //get reps of this user
        if (repositoriesList?.isEmpty()!!) {
            viewState.showError(R.string.login_error)
        } else {
            viewState.setupRepositoriesList(repositoriesList)
        }
        viewState.endLoading()
    }

    fun openStars(userName: String, repository: Repository?) {
        if (repository != null) {
            viewState.openStars(userName, repository)
        }
    }

    fun showError(textResource: Int) {
        viewState.endLoading()
        viewState.showError(textResource)
    }
}