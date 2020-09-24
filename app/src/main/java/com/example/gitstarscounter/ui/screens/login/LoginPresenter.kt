package com.example.gitstarscounter.ui.screens.login

import android.content.Context
import android.util.Log
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.providers.login.LoginRepository
import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.example.gitstarscounter.data.repository.remote.RequestLimit
import com.example.gitstarscounter.data.repository.remote.entity.RemoteRepository
import com.example.gitstarscounter.data.repository.remote.entity.RemoteUser
import com.example.gitstarscounter.data.repository.remote.entity.resource_remote.ResourceRemote
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.example.gitstarscounter.ui.screens.repository.RepositoryActivity
import com.example.gitstarscounter.ui.screens.stars.StarsActivity.Companion.createLauncher
import com.omega_r.base.errors.AppException
import com.omega_r.libs.omegatypes.Text
import com.omegar.mvp.InjectViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@InjectViewState
class LoginPresenter : BasePresenter<LoginView>() {
    companion object {
        const val TAG = "LoginPresenter"
        const val MESSAGE = "limited"
        const val MAX_PACKAGE_ELEMENTS_COUNT = 30
    }

    private val repositoryLoginProvider = LoginRepository()
    private var userName = ""
    private var isLimited = false

    @Inject
    lateinit var git: GithubApiService

    init {
        GitStarsApplication.instance.gitStarsCounterComponent.inject(this)
    }

    fun responseToLoadRepositories(userName: String, pageNumber: Int) {
        viewState.endPagination()
        this.userName = userName
        RequestLimit.writeLog()
        Log.d(TAG, userName)
        launch {
            if (userName != "") {
                //  updateResourceLimit()
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
        createLauncher(
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
        viewState.setupRepositoriesList(repositoryRemoteList)

        if (repositoryRemoteList.size < MAX_PACKAGE_ELEMENTS_COUNT) {
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

    fun responseToStartRepositoryActivity(context: Context) {
        RepositoryActivity.createLauncher().launch(context)
    }

    private fun onUnknownUser(textResource: Int, noInternetIsVisible: Boolean) {
        viewState.endPagination()
        viewState.setWaiting(false)
        viewState.changeVisibilityOfNoInternetView(noInternetIsVisible && !isLimited)
        viewState.changeVisibilityOfLimitedView(isLimited)
        viewState.showMessage(Text.from(textResource))
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
