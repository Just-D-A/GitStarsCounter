package com.example.gitstarscounter.ui.screens.login

import android.content.Context
import android.util.Log
import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.to_rename_2.remote.RequestLimit
import com.example.gitstarscounter.data.to_rename_2.remote.entity.RemoteRepository
import com.example.gitstarscounter.data.to_rename_2.remote.entity.RemoteUser
import com.example.gitstarscounter.data.to_rename_2.remote.entity.resource_remote.ResourceRemote
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.data.for_providers.login.LoginRepository
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.example.gitstarscounter.ui.screens.stars.StarsActivity
import com.omega_r.base.errors.AppException
import com.omega_r.libs.omegatypes.Text
import com.omegar.mvp.InjectViewState
import kotlinx.coroutines.launch

@InjectViewState
class LoginPresenter : BasePresenter<LoginView>() {
    companion object {
        const val TAG = "LoginPresenter"
        const val MESSAGE = "limited"
    }

    private val repositoryLoginProvider = LoginRepository()
    private var userName = ""
    private var isLimited = false

    init {
        launch {
            val resourceRemote = repositoryLoginProvider.getLimitRemaining()
            if (resourceRemote != null) {
                onLimitRemaining(resourceRemote)
            } else {
                onLimitedError()
            }
        }
    }

    fun responseToLoadRepositories(userName: String, pageNumber: Int) {
        viewState.endPagination()
        this.userName = userName
        Log.d(TAG, userName)
        launch {
            if (userName != "") {
                updateResourceLimit()
                isLimited = false
                try {
                    Log.d(TAG, "todo")
                    val remoteRepositoryList =
                        repositoryLoginProvider.getRemoteUsersRepositories(userName, pageNumber)
                    onLoginResponse(remoteRepositoryList!!, false)
                } catch (e: AppException.NoData) {
                    onUnknownUser(R.string.unknown_user_text, false)
                }
            }
        }
    }

    private fun updateResourceLimit() {
        launch {
            val resourceRemote = repositoryLoginProvider.getLimitRemaining()
            if (resourceRemote != null) {
                onLimitRemaining(resourceRemote)
            } else {
                onLimitedError()
            }
        }
    }

    fun responseToLoadMoreRepositories(pageNumber: Int) {
        if (userName.isNotEmpty()) {
            Log.d("PAGINATION", "LOAD")
            if (RequestLimit.hasRequest()) {
                launch {
                    val moreRepositories =
                        repositoryLoginProvider.loadMoreRepositories(userName, pageNumber)
                    onGetMoreRepositories(moreRepositories)
                    isLimited = false
                }
            } else if (!isLimited) {
                showLimitedMessage()
            } else {
                viewState.endPagination()
            }
        }
    }

    fun responseToOpenStars(context: Context, repository: Repository?) {
        StarsActivity.createLauncher(
            userName,
            RemoteRepository(
                id = repository!!.id,
                name = repository.name,
                allStarsCount = repository.allStarsCount,
                user = RemoteUser(
                    repository.user.id,
                    repository.user.name,
                    repository.user.avatarUrl
                )
            )
        )
            .launch(context)
    }

    private fun onLoginResponse(
        repositoryRemoteList: List<Repository>,
        noInternetIsVisible: Boolean
    ) {
        /* viewState.changeVisibilityOfNoInternetView(noInternetIsVisible && !isLimited)
         viewState.changeVisibilityOfLimitedView(isLimited)*/
        viewState.setupRepositoriesList(repositoryRemoteList)

        if (repositoryRemoteList.size < 30) {
            viewState.endPagination()
        }
    }

    private fun onError() {
        Log.d(TAG, "onError complite")
        viewState.setWaiting(false)
        viewState.endPagination()
        isLimited = false
    }

    private fun onLimitedError() {
        isLimited = false
    }

    private fun onUnknownUser(textResource: Int, noInternetIsVisible: Boolean) {
        viewState.endPagination()
        viewState.setWaiting(false)
        viewState.changeVisibilityOfNoInternetView(noInternetIsVisible && !isLimited)
        viewState.changeVisibilityOfLimitedView(isLimited)
        viewState.showMessage(Text.from(textResource))
    }

    private fun onLimitRemaining(resourceRemote: ResourceRemote) {
        RequestLimit.setLimitResourceCount(resourceRemote.resources.core.remaining)
        RequestLimit.writeLog()
        if (!RequestLimit.hasRequest()) {
            isLimited = true
        }
    }

    private fun onGetMoreRepositories(repositoriesRemote: List<Repository>?) {
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

    private fun showLimitedMessage() {
        isLimited = true
        Log.d(TAG, MESSAGE)
        viewState.endPagination()
    }
}
