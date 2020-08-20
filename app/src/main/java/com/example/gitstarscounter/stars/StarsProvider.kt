package com.example.gitstarscounter.stars

import com.example.gitstarscounter.retrofit2.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Suppress("DEPRECATION")
class StarsProvider(var presenter: StarsPresenter) {
   /* fun testLoadStars() {
        //использовать BackgroundThread
        Handler().postDelayed({
            val starsList: ArrayList<StarModel> = ArrayList()

            val star1 = StarModel(
                count = 1,
                date = Date(2020, 1, 4)
            )
            val star2 = StarModel(
                count = 2,
                date = Date(2020, 5, 4)
            )
            val star3 = StarModel(
                count = 5,
                date = Date(2020, 12, 4)

            )
            Log.d("Provider", star1.date.year.toString())

            starsList.add(star1)
            starsList.add(star2)
            starsList.add(star3)



            presenter.loadGrafic(starsList)
        }, 200)
    }
*/
    fun loadStars(userName: String) {
        val repository = SearchRepositoryProvider.provideSearchRepository()
        repository.getStars(userName)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ result ->
                presenter.loadGrafic(result)
            }, { error ->
                presenter.showError(error as Exception)
                // error.printStackTrace()
            })
    }
}