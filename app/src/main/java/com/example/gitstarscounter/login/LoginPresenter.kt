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
            viewState.changeVisibilityOfLimitedView(false)
        } else {
            showLimitedMessage()
        }
    }

    fun loadMoreRepositories(pageNumber: Int) {
        if (userName.isNotEmpty()) {
            Log.d("PAGINATION", "LOAD")
            if (limitResourceCount > 0) {
                loginProvider.loadMoreRepositories(userName, pageNumber, this)
                limitResourceCount--
            } else {
                showLimitedMessage()
            }
        }
    }

    fun openStars(repository: RepositoryModel?) {
        if(limitResourceCount > 0) {
            viewState.changeVisibilityOfLimitedView(false)
        } else {
            showLimitedMessage()
        }
        viewState.openStars(repository!!.user.login, repository, limitResourceCount)
    }

    override fun onLoginResponse(
        repositoryModelList: List<RepositoryModel>,
        noInternetIsVisible: Boolean
    ) {
        //viewState.changeVisibilityOfLimitedView(noInternetIsVisible)
        viewState.setupRepositoriesList(repositoryModelList)
        if (repositoryModelList.size < 30) {
            viewState.endPagination()
        }
    }

    override fun onError(textResource: Int) {
        viewState.endLoading()
        viewState.changeVisibilityOfNoInternetView(true)
        getFromEntity()
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

    private fun showLimitedMessage() {
        Log.d(TAG, MESSAGE)
        getFromEntity()
        viewState.endPagination()
        viewState.endLoading()
    }

    private fun getFromEntity() {
        val loginEntityProvider = LoginEntityProvider(this)
        loginEntityProvider.getUsersRepositories(userName)
    }

    companion object {
        const val TAG = "LoginPresenter"
        const val MESSAGE = "limited"
    }
}
