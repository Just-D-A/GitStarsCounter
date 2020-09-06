package com.example.gitstarscounter.login


import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.SearchProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST")
class LoginProvider() {
    fun loadUser(userName: String, pageNumber: Int, loginCallback: LoginCallback) {
        val repository = SearchProvider.provideSearchRepository()
        val repositoriesList = repository.getUserRepos(userName, pageNumber)

        repositoriesList.enqueue(object : Callback<List<RepositoryModel?>?> {
            override fun onResponse(
                call: Call<List<RepositoryModel?>?>?,
                response: Response<List<RepositoryModel?>?>?
            ) {
                if (response?.body() != null) {
                    loginCallback.onLoginResponse(response.body() as List<RepositoryModel>, false)
                } else {
                    loginCallback.onError(R.string.unknown_user_text)// maybe unknown user fun ?????????
                }
            }

            override fun onFailure(call: Call<List<RepositoryModel?>?>?, t: Throwable?) {
                loginCallback.onError(R.string.no_internet_text)
            }
        })
    }

    fun loadMoreRepositories(userName: String, pageNumber: Int, callback: RepositoryAdapter.Callback) {
        val repository = SearchProvider.provideSearchRepository()
        val repositoriesList = repository.getUserRepos(userName, pageNumber)

        repositoriesList.enqueue(object : Callback<List<RepositoryModel?>?> {
            override fun onResponse(
                call: Call<List<RepositoryModel?>?>?,
                response: Response<List<RepositoryModel?>?>?
            ) {
                    callback.onGetMoreRepositories(response?.body() as List<RepositoryModel>?)
            }

            override fun onFailure(call: Call<List<RepositoryModel?>?>?, t: Throwable?) {
                callback.onGetMoreRepositories(null)
            }
        })
    }
}
