package com.example.gitstarscounter.service

import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.SearchProvider
import com.example.gitstarscounter.git_api.StarModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceProvider() {
    private val searchRepository = SearchProvider.provideSearchStars()

    fun loadStars(
        userName: String,
        repositoryModel: RepositoryModel,
        pageNumber: Int,
        serviceCallback: ServiceCallback,
        starsList: MutableList<StarModel>
    ) {
        val starsListGet = searchRepository.getStars(userName, repositoryModel.name, pageNumber)
        starsListGet.enqueue(object : Callback<List<StarModel?>?> {
            override fun onResponse(call: Call<List<StarModel?>?>, response: Response<List<StarModel?>?>) {
                if (response.body() != null) {
                    starsList.addAll(response.body() as List<StarModel>)
                    serviceCallback.onStarsResponse(starsList, repositoryModel ,pageNumber)
                }
            }

            override fun onFailure(call: Call<List<StarModel?>?>, t: Throwable) {
                serviceCallback.onError(R.string.no_internet_text)
            }
        })
    }
}
//alarm sevice