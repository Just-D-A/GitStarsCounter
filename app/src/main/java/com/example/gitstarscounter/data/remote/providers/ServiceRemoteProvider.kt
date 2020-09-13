package com.example.gitstarscounter.data.remote.providers

import android.util.Log
import com.example.gitstarscounter.data.remote.entity.RepositoryRemote
import com.example.gitstarscounter.data.remote.SearchProvider
import com.example.gitstarscounter.data.remote.entity.StarRemote
import com.example.gitstarscounter.entity.RepositoryModel

class ServiceRemoteProvider() {
    private val searchRepository = SearchProvider.provideSearchStars()

    fun loadStars(
        userName: String,
        repositoryRemote: RepositoryModel,
        pageNumber: Int
    ): List<StarRemote?> {
        val starsListGet = searchRepository.getStars(userName, repositoryRemote.name, pageNumber)

        val response = starsListGet.execute()
        return if(response.body() != null) {
            response.body()!!
        } else {
            Log.d("SERVICE_PROVIDER", "INTERNET ERROR")
            emptyList()
        }
        /*(
            object : Callback<List<StarModel?>?> {//to exequte
            override fun onResponse(
                call: Call<List<StarModel?>?>,
                response: Response<List<StarModel?>?>
            ) {
                if (response.body() != null) {
                    starsList.addAll(response.body() as List<StarModel>)
                    serviceCallback.onStarsResponse(starsList, repositoryModel, pageNumber)
                }
            }

            override fun onFailure(call: Call<List<StarModel?>?>, t: Throwable) {
                serviceCallback.onError(R.string.no_internet_text)
            }
        })*/
    }
}
