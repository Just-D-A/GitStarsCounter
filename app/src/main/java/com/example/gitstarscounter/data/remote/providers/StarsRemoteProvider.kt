package com.example.gitstarscounter.data.remote.providers

import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.remote.SearchProvider
import com.example.gitstarscounter.data.remote.entity.StarRemote
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.ui.screens.stars.StarsCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION", "UNCHECKED_CAST")
class StarsRemoteProvider {
    private val searchRepository = SearchProvider.provideSearchStars()

    fun loadStars(
        userName: String,
        repositoryRemote: RepositoryModel,
        pageNumber: Int,
        starsCallback: StarsCallback
    ) {
        val starsList = searchRepository.getStars(userName, repositoryRemote.name, pageNumber)
        starsList.enqueue(object : Callback<List<StarRemote?>?> {
            override fun onResponse(
                call: Call<List<StarRemote?>?>,
                response: Response<List<StarRemote?>?>
            ) {
                if (response.body() != null) {
                    starsCallback.onStarsResponse(response.body() as List<StarRemote>, false)
                }
            }

            override fun onFailure(call: Call<List<StarRemote?>?>, t: Throwable) {
                starsCallback.onError(R.string.no_internet_text)
            }
        })
    }
}
