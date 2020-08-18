package com.example.gitstarscounter.stars

import android.os.Handler

class StarsProvider(var presenter: StarsPresenter) {
    fun testLoadFriends() {
        //использовать BackgroundThread
        Handler().postDelayed({
            val starList: ArrayList<StarModel> = ArrayList()

            val star1 = StarModel(
                count = 2,
                date = "2020-12-12"
            )
            val star2 = StarModel(
                count = 3,
                date = "11-02-2020"
            )
            val star3 = StarModel(
                count = 3,
                date = "12-02-2020"
            )

            starList.add(star1)
            starList.add(star2)
            starList.add(star3)

            presenter.loadStars()
        }, 200)
    }
}