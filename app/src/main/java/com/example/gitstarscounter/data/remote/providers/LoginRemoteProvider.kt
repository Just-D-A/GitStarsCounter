package com.example.gitstarscounter.data.remote.providers

import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.remote.SearchProvider
import com.example.gitstarscounter.data.remote.entity.RepositoryRemote
import com.example.gitstarscounter.data.remote.entity.ResourceRemote
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.ui.screens.login.LoginCallback
import com.example.gitstarscounter.ui.screens.login.RepositoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST")
class LoginRemoteProvider() {
    fun loadRepositories(userName: String, pageNumber: Int, loginCallback: LoginCallback) {
        val repository = SearchProvider.provideSearchRepository()
        val repositoriesList = repository.getUserRepos(userName, pageNumber)

        repositoriesList.enqueue(object : Callback<List<RepositoryRemote?>?> {
            override fun onResponse(
                call: Call<List<RepositoryRemote?>?>?,
                response: Response<List<RepositoryRemote?>?>?
            ) {
                if (response?.body() != null) {
                    loginCallback.onLoginResponse(response.body() as List<RepositoryRemote>, false)
                } else {
                    loginCallback.onUnknownUser(R.string.unknown_user_text, false)
                }
            }

            override fun onFailure(call: Call<List<RepositoryRemote?>?>?, t: Throwable?) {
                loginCallback.onError()
            }
        })
    }

    fun loadMoreRepositories(
        userName: String,
        pageNumber: Int,
        callback: RepositoryAdapter.Callback
    ) {
        val loginSearch = SearchProvider.provideSearchRepository()
        val repositoriesList = loginSearch.getUserRepos(userName, pageNumber)

        repositoriesList.enqueue(object : Callback<List<RepositoryRemote?>?> {
            override fun onResponse(
                call: Call<List<RepositoryRemote?>?>?,
                response: Response<List<RepositoryRemote?>?>?
            ) {
                callback.onGetMoreRepositories(response?.body() as List<RepositoryRemote>?)
            }

            override fun onFailure(call: Call<List<RepositoryRemote?>?>?, t: Throwable?) {
                callback.onGetMoreRepositories(null)
            }
        })
    }

    fun getLimitRemaining(loginCallback: LoginCallback) {
        val loginSearch = SearchProvider.provideSearchRepository()
        val limitRemaining = loginSearch.getLimitRemaining()

        limitRemaining.enqueue(object : Callback<ResourceRemote> {
            override fun onResponse(
                call: Call<ResourceRemote>,
                response: Response<ResourceRemote>
            ) {
                loginCallback.onLimitRemaining(response.body()!!)
            }

            override fun onFailure(call: Call<ResourceRemote>, t: Throwable) {
                loginCallback.onLimitedError()
            }
        })
    }
}
