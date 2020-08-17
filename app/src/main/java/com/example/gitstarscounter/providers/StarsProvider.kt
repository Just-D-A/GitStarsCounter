package com.example.gitstarscounter.providers

import android.os.Handler
import com.example.gitstarscounter.models.StarModel
import com.example.gitstarscounter.presenters.StarsPresenter

class StarsProvider(var presenter: StarsPresenter) {
    fun testLoadFriends() {
        Handler().postDelayed({
            val starList: ArrayList<StarModel> = ArrayList()

            val star1 = StarModel(count = 2, date = "10-02-2020")
            val star2 = StarModel(count = 3, date = "11-02-2020")
            val star3 = StarModel(count = 3, date = "12-02-2020")

            starList.add(star1)
            starList.add(star2)
            starList.add(star3)

            presenter.loadStars()
        }, 200)
    }
}