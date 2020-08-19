package com.example.gitstarscounter.stars

import android.os.Handler
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class StarsProvider(var presenter: StarsPresenter) {
    fun testLoadStars() {
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
}