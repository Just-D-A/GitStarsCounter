package com.example.gitstarscounter.ui.screens.login

import android.util.Log
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.providers.login.LoginRepository
import com.example.gitstarscounter.data.repository.remote.RequestLimit
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
        private const val TAG = "LoginPresenter"
        private const val MAX_PACKAGE_ELEMENTS_COUNT = 30
    }

    @Inject
    lateinit var repositoryLoginProvider: LoginRepository

    private var userName = ""
    private var isLimited = false

    init {
        GitStarsApplication.instance.gitStarsCounterComponent.inject(this)
    }

    fun requestToLoadRepositories(userName: String, pageNumber: Int) {
        this.userName = userName
        RequestLimit.writeLog()
        launchWithWaiting {
            if (userName != "") {
                isLimited = false
                try {
                    Log.d(TAG, "todo")
                    val repositoryList =
                        repositoryLoginProvider.getUsersRepositories(userName, pageNumber)
                    viewState.setupRepositoriesList(repositoryList)
                    if (repositoryList.size < MAX_PACKAGE_ELEMENTS_COUNT) {
                        viewState.endPagination()
                    }
                } catch (e: AppException.NoData) {
                    viewState.endPagination()
                    viewState.showMessage(Text.from(R.string.unknown_user_text))
                }
            }
        }
    }

    fun requestToLoadMoreRepositories(pageNumber: Int) {
        if (userName.isNotEmpty()) {
            launch {
                try {
                    val moreRepositories =
                        repositoryLoginProvider.getMoreUsersRepositories(userName, pageNumber)
                    viewState.addRepositoriesToList(moreRepositories)
                    if (moreRepositories.size < MAX_PACKAGE_ELEMENTS_COUNT) {
                        viewState.endPagination()
                    }
                } catch (e: AppException.NoData) {
                    viewState.endPagination()
                }
            }
        }
    }

    fun requestToOpenStars(repository: Repository) {
        createLauncher(userName, repository).launch()
    }

    fun requestToStartRepositoryActivity() {
        RepositoryActivity.createLauncher().launch()
    }
}
