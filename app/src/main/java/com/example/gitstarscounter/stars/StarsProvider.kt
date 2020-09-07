package com.example.gitstarscounter.stars

import android.util.Log
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.SearchProvider
import com.example.gitstarscounter.git_api.StarModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION", "UNCHECKED_CAST")
class StarsProvider {
    private val searchRepository = SearchProvider.provideSearchStars()

    fun loadStars(
        userName: String,
        repositoryModel: RepositoryModel,
        pageNumber: Int,
        starsCallback: StarsCallback
    ) {
        val starsList = searchRepository.getStars(userName, repositoryModel.name, pageNumber)
        starsList.enqueue(object : Callback<List<StarModel?>?> {
            override fun onResponse(
                call: Call<List<StarModel?>?>,
                response: Response<List<StarModel?>?>
            ) {
                if (response.body() != null) {
                    starsCallback.onStarsResponse(response.body() as List<StarModel>, false)
                }
            }

            override fun onFailure(call: Call<List<StarModel?>?>, t: Throwable) {
                starsCallback.onError(R.string.no_internet_text)
            }
        })
    }
}
