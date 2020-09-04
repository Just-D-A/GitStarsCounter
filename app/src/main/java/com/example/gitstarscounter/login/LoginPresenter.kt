package com.example.gitstarscounter.login

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.entity.repository.Repository
import com.example.gitstarscounter.git_api.RepositoryModel

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@InjectViewState
class LoginPresenter : MvpPresenter<LoginView>(), LoginCallback, RepositoryAdapter.Callback {

    private val loginProvider = LoginProvider()
    private var userName = ""


    fun loadRepositories(userName: String, pageNumber: Int) {
    //    viewState.startLoading()
        this.userName = userName
        loginProvider.loadUser(userName, 1, this)
    }

    fun loadMoreRepositories(pageNumber: Int) {
        if (!userName.isEmpty()) {
            Log.d("PAGINATION", "LOAD")
            loginProvider.loadMoreRepositories(userName, pageNumber, this)
        }
    }

    fun openStars(repository: RepositoryModel?) {
        viewState.openStars(repository!!.user.login, repository)
    }

    override fun onLoginResponse(
        repositoryList: List<RepositoryModel>,
        noInternetIsVisible: Boolean
    ) {
        viewState.changeVisibilityOfNoInternetView(noInternetIsVisible)
        viewState.setupRepositoriesList(repositoryList)
     //   viewState.endLoading()
        if(repositoryList.size < 30) {
            viewState.endPagination()
        }
    }


    override fun onError(textResource: Int) {
        viewState.endLoading()
        viewState.changeVisibilityOfNoInternetView(false)
        val loginEntityProvider = LoginEntityProvider(this)
        loginEntityProvider.getUsersRepositories(userName)

    }

    override fun onGetMoreRepositories(repositoriesModelList: List<RepositoryModel>?) {
        Log.d("PAGINATION", "GETTED")
        if(repositoriesModelList != null) {
            viewState.addPagination(repositoriesModelList)
            if (repositoriesModelList.size < 30) {
                viewState.endPagination()
            }
        } else {
            viewState.endPagination()
        }
    }

}