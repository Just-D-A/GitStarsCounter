package com.example.gitstarscounter.login

import android.util.Log
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.SearchProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LoginProvider() {
    //добавить интерфейс реализации для presenter
    fun loadUser(userName: String, loginCallback: LoginCallback) {
        val repository = SearchProvider.provideSearchRepository()
        val repositoriesList = repository.getUserRepos(userName)

        repositoriesList.enqueue(object : Callback<List<Repository?>?> {
            override fun onResponse(
                call: Call<List<Repository?>?>?,
                response: Response<List<Repository?>?>?
            ) {
                loginCallback.onLoginResponse(response?.body())
            }

            override fun onFailure(call: Call<List<Repository?>?>?, t: Throwable?) {
                loginCallback.onError(R.string.no_internet_text)
            }
        })
    }
}

//

/*old fun
  fun loadUser(userName: String) {
        val repository = SearchProvider.provideSearchRepository()
        val repositoriesList = repository.getUserRepos(userName)

        repositoriesList.enqueue(object : Callback<List<Repository?>?> {
            override fun onResponse(call: Call<List<Repository?>?>?, response: Response<List<Repository?>?>?) {
                if (response?.isSuccessful!!) {
                    Log.d("LoginProvider: ", "ALL GOOD")
                    presenter.getRepositories(response.body())
                } else {
                    presenter.showError(R.string.unknown_user_text)
                }
            }

            override fun onFailure(call: Call<List<Repository?>?>?, t: Throwable?) {
                presenter.showError(R.string.no_internet_text)
            }
        })
    }
 */

