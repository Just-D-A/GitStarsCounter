package com.example.gitstarscounter.stars

import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.SearchProvider
import com.example.gitstarscounter.git_api.Star
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class StarsProvider(val starsCallback: StarsCallback) {

    private val searchRepository = SearchProvider.provideSearchStars()

    fun loadStars(userName: String, repository: Repository, pageNumber: Int) { // без rx сделать так чтобы работало

        val starsList = searchRepository.getStars(userName, repository.name, pageNumber)
        starsList.enqueue(object : Callback<List<Star?>?> {
            override fun onResponse(call: Call<List<Star?>?>, response: Response<List<Star?>?>) {
                starsCallback.getStarsResponse(response.body())
            }

            override fun onFailure(call: Call<List<Star?>?>, t: Throwable) {
                starsCallback.getError(t)
            }

        })
        /*yearOfLastELement = starsCallback.getYearOfLastStar()
        currStarsCount = starsCallback.getStrasListSize()
        Log.d("IN_WHILE_CURR_COUNT: " , currStarsCount.toString())
        Log.d("IN_WHILE_LAST_YEAR: " , currStarsCount.toString())*/
        //  }while((allStarsCount > currStarsCount) && (yearOfLastELement <= currYear) && (!error))
        //starsCallback.loadGraph(error)
    }
}

/* old fun

    fun loadStars(userName: String, repository: Repository) { // без rx сделать так чтобы работало
      //  val allStarsCount =  repository.getStarsCount(userName, repositoryName)
        Log.d("All_Stars_Count", repository.allStarsCount.toString())

        //do
        val starsList = searchRepository.getStars(userName, repository.name)
        starsList.enqueue(object : Callback<List<Star?>?> {
            override fun onResponse(call: Call<List<Star?>?>, response: Response<List<Star?>?>) {
                if (response.isSuccessful) {
                    presenter.loadGrafic(response.body())
                } else {
                    presenter.showError(R.string.unknown_user_text)
                    //break

                }
            }

            override fun onFailure(call: Call<List<Star?>?>, t: Throwable) {
                presenter.showError(R.string.no_internet_text)
                //break
            }
        })
     //   while((allStarsCount > currStarsCount) && (yearOfLastELement > currYear))
    }
 */