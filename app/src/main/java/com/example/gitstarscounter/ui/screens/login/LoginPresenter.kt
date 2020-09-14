package com.example.gitstarscounter.ui.screens.login

import android.content.Context
import android.util.Log
import com.example.gitstarscounter.data.local.providers.LoginLocalProvider
import com.example.gitstarscounter.data.remote.RequestLimit
import com.example.gitstarscounter.data.remote.entity.RepositoryRemote
import com.example.gitstarscounter.data.remote.entity.ResourceRemote
import com.example.gitstarscounter.data.remote.entity.UserRemote
import com.example.gitstarscounter.data.remote.providers.LoginRemoteProvider
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.example.gitstarscounter.ui.screens.stars.StarsActivity
import com.omega_r.libs.omegatypes.Text
import com.omegar.mvp.InjectViewState


@InjectViewState
class LoginPresenter : BasePresenter<LoginView>(), LoginCallback, RepositoryAdapter.Callback {
    private val loginProvider = LoginRemoteProvider()
    private var userName = ""
    private var isLimited = false

    init {
        loginProvider.getLimitRemaining(this)
    }

    fun responseToLoadRepositories(userName: String, pageNumber: Int) {
        viewState.endPagination()
        this.userName = userName
        loginProvider.getLimitRemaining(this)
        if (RequestLimit.limitResourceCount > 0) {
            loginProvider.loadRepositories(userName, pageNumber, this)
            RequestLimit.subtractLimitResourceCount()
            isLimited = false
        } else {
            loginProvider.getLimitRemaining(this)
            showLimitedMessage()
        }
    }

    fun responseToLoadMoreRepositories(pageNumber: Int) {
        if (userName.isNotEmpty()) {
            Log.d("PAGINATION", "LOAD")
            if (RequestLimit.limitResourceCount > 0) {
                loginProvider.loadMoreRepositories(userName, pageNumber, this)
                RequestLimit.subtractLimitResourceCount()
                isLimited = false
            } else if (!isLimited) {
                showLimitedMessage()
            } else {
                viewState.endPagination()
            }
        }
    }

    fun responseToOpenStars(context: Context, repository: RepositoryModel?) {
        StarsActivity.createLauncher(
            userName,
            RepositoryRemote(
                id = repository!!.id,
                name = repository.name,
                allStarsCount = repository.allStarsCount,
                user = UserRemote(
                    repository.user.id,
                    repository.user.name,
                    repository.user.avatarUrl
                )
            ),
            RequestLimit.limitResourceCount
        )
            .launch(context)
    }

    override fun onLoginResponse(
        repositoryRemoteList: List<RepositoryModel>,
        noInternetIsVisible: Boolean
    ) {
        viewState.changeVisibilityOfNoInternetView(noInternetIsVisible && !isLimited)
        viewState.changeVisibilityOfLimitedView(isLimited)
        viewState.setupRepositoriesList(repositoryRemoteList as List<RepositoryModel>)

        if (repositoryRemoteList.size < 30) {
            viewState.endPagination()
        }
    }

    override fun onError() {
        viewState.setWaiting(false)
        isLimited = false
        //viewState.changeVisibilityOfNoInternetView(true)
        getFromLocal()
    }

    override fun onLimitedError() {
        isLimited = false
    }

    override fun onUnknownUser(textResource: Int, noInternetIsVisible: Boolean) {
        //   viewState.showError(textResource)
        viewState.changeVisibilityOfNoInternetView(noInternetIsVisible && !isLimited)
        viewState.changeVisibilityOfLimitedView(isLimited)
        viewState.showMessage(Text.from(textResource))
        //viewState.hideQueryOrMessage()
    }

    override fun onLimitRemaining(resourceRemote: ResourceRemote) {
        RequestLimit.limitResourceCount = resourceRemote.resources.core.remaining
        Log.d("LIMIT", RequestLimit.limitResourceCount.toString())
        if (RequestLimit.limitResourceCount == 0) {
            isLimited = true
        }
    }

    override fun onGetMoreRepositories(repositoriesRemote: List<RepositoryModel>?) {
        Log.d("PAGINATION", "GETTED")
        if (repositoriesRemote != null) {
            viewState.addPagination(repositoriesRemote)
            if (repositoriesRemote.size < 30) {
                viewState.endPagination()
            }
        } else {
            viewState.endPagination()
        }
    }

    fun setLimitResourceCount(limitResourceCount: Int) {
        Log.d(TAG, limitResourceCount.toString() + "GETTED")
        RequestLimit.limitResourceCount = limitResourceCount
    }

    private fun showLimitedMessage() {
        isLimited = true
        Log.d(TAG, MESSAGE)
        viewState.endPagination()
        getFromLocal()
        //    viewState.changeVisibilityOfLimitedView(isLimited)
    }

    private fun getFromLocal() {
        val loginEntityProvider = LoginLocalProvider(this)
        loginEntityProvider.getUsersRepositories(userName)
    }

    companion object {
        const val TAG = "LoginPresenter"
        const val MESSAGE = "limited"
    }
}
