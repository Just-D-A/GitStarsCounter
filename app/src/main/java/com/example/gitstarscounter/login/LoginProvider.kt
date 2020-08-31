package com.example.gitstarscounter.login

import android.util.Log
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.SearchProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST")
class LoginProvider() {
    fun loadUser(userName: String, loginCallback: LoginCallback) {
        val repository = SearchProvider.provideSearchRepository()
        val repositoriesList = repository.getUserRepos(userName)

        repositoriesList.enqueue(object : Callback<List<Repository?>?> {
            override fun onResponse(call: Call<List<Repository?>?>?, response: Response<List<Repository?>?>?) {
                if (response?.body() != null) {
                    loginCallback.onLoginResponse(response.body() as List<Repository>)
                } else {
                    loginCallback.onError(R.string.unknown_user_text)// maybe unknown user fun ?????????
                }
            }

            override fun onFailure(call: Call<List<Repository?>?>?, t: Throwable?) {
                loginCallback.onError(R.string.no_internet_text)
            }
        })
    }
}