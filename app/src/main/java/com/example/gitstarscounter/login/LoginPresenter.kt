package com.example.gitstarscounter.login

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.ResourceModel

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@InjectViewState
class LoginPresenter : MvpPresenter<LoginView>(), LoginCallback, RepositoryAdapter.Callback {
    private val loginProvider = LoginProvider()
    private var userName = ""
    private var limitResourceCount: Int = 0

    init {
        loginProvider.getLimitRemaining(this)
    }

    fun loadRepositories(userName: String, pageNumber: Int) {
        this.userName = userName
        if (limitResourceCount > 0) {
            loginProvider.loadUser(userName, pageNumber, this)
            limitResourceCount--
        } else {
            Log.d(TAG, MESSAGE)
        }
    }

    fun loadMoreRepositories(pageNumber: Int) {
        if (userName.isNotEmpty()) {
            Log.d("PAGINATION", "LOAD")
            if (limitResourceCount > 0) {
                loginProvider.loadMoreRepositories(userName, pageNumber, this)
                limitResourceCount--
            } else {
                Log.d(TAG, MESSAGE)
            }
        }

    }

    fun openStars(repository: RepositoryModel?) {
        viewState.openStars(repository!!.user.login, repository, limitResourceCount)
    }

    override fun onLoginResponse(
        repositoryModelList: List<RepositoryModel>,
        noInternetIsVisible: Boolean
    ) {
        viewState.changeVisibilityOfNoInternetView(noInternetIsVisible)
        viewState.setupRepositoriesList(repositoryModelList)
        if (repositoryModelList.size < 30) {
            viewState.endPagination()
        }
    }

    override fun onError(textResource: Int) {
        viewState.endLoading()
        viewState.changeVisibilityOfNoInternetView(false)
        val loginEntityProvider = LoginEntityProvider(this)
        loginEntityProvider.getUsersRepositories(userName)
    }

    override fun onLimitRemaining(resourceModel: ResourceModel) {
        limitResourceCount = resourceModel.resources.core.remaining
        Log.d("LIMIT", limitResourceCount.toString())
    }

    override fun onGetMoreRepositories(repositoriesModelList: List<RepositoryModel>?) {
        Log.d("PAGINATION", "GETTED")
        if (repositoriesModelList != null) {
            viewState.addPagination(repositoriesModelList)
            if (repositoriesModelList.size < 30) {
                viewState.endPagination()
            }
        } else {
            viewState.endPagination()
        }
    }

    fun setLimitResourceCount(limitResourceCount: Int) {
        Log.d(TAG, limitResourceCount.toString() + "GETTED")
        this.limitResourceCount = limitResourceCount
    }

    companion object {
        const val TAG = "LoginPresenter"
        const val MESSAGE = "limited"
    }
}
