package com.example.gitstarscounter.stars

import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.SearchProvider
import com.example.gitstarscounter.git_api.Star
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class StarsProvider(var presenter: StarsPresenter) {

    fun loadStars(userName: String, repositoryName: String) { // без rx сделать так чтобы работало
        val repository = SearchProvider.provideSearchStars()
        val starsList = repository.getStars(userName, repositoryName)

        starsList.enqueue(object : Callback<List<Star?>?> {
            override fun onResponse(call: Call<List<Star?>?>, response: Response<List<Star?>?>) {
                if (response.isSuccessful) {
                    presenter.loadGrafic(response.body())
                } else {
                    presenter.showError(R.string.unknown_user_text)
                }
            }

            override fun onFailure(call: Call<List<Star?>?>, t: Throwable) {
                presenter.showError(R.string.no_internet_text)
            }
        })
    }
}