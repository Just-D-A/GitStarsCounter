package com.example.gitstarscounter.stars

import android.util.Log
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Star

interface StarsCallback {

    fun onStarsResponse(responseStarsList: List<Star?>?)
    /*    if (responseStarsList != null) {
            responseStarsList.forEach {
                Log.d("StarsCallback", it?.user?.login)
                starsList.add(it!!)
            }
            needMore()
            //presenter.loadGrafic(responseStarsList)
        } else {
            presenter.showError(R.string.unknown_user_text)
            error = true
        }*/


    /*private fun needMore() {
        var lastStarYear = 0
        if (starsList.size != 0) {
            lastStarYear = starsList[starsList.size - 1].starred_at.year
        }
        val currYear = presenter.currYear
        val currStarsCount = starsList.size
        val allStarsCount = presenter.repository.allStarsCount
        if ((lastStarYear <= currYear) && (currStarsCount < allStarsCount) && (!error)) {
            pageNumber++
            presenter.loadMoreStars(pageNumber)
        } else {
            loadGraph()
        }
    }*/

    fun onError(textResource: Int)
        /*error = true
        presenter.showError(R.string.no_internet_text)*/


    /*fun getStrasListSize(): Int {
        return starsList.size
    }*

    fun getYearOfLastStar(): Int {
        if (starsList.isEmpty()) {
            return 0
        } else {
            return starsList[starsList.size - 1].starred_at.year
        }
    }

    fun loadGraph() {
        if (!error) {
            presenter.loadGrafic(starsList)
        }
    }*/

    /*fun getStrasList(): List<Star> {
        return starsList
    }*/
}


