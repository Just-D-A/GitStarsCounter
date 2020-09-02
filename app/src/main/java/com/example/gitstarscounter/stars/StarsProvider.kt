package com.example.gitstarscounter.stars

import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.SearchProvider
import com.example.gitstarscounter.git_api.Star
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION", "UNCHECKED_CAST")
class StarsProvider() {

    private val searchRepository = SearchProvider.provideSearchStars()

    fun loadStars(userName: String, repository: Repository, pageNumber: Int, starsCallback: StarsCallback) { // без rx сделать так чтобы работало

        val starsList = searchRepository.getStars(userName, repository.name, pageNumber)
        starsList.enqueue(object : Callback<List<Star?>?> {
            override fun onResponse(call: Call<List<Star?>?>, response: Response<List<Star?>?>) {
                if(response.body() != null) {
                    starsCallback.onStarsResponse(response.body() as List<Star>, false)
                }
            }

            override fun onFailure(call: Call<List<Star?>?>, t: Throwable) {
                starsCallback.onError(R.string.no_internet_text)
            }
        })
    }
}
