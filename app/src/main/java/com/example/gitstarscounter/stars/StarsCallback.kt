package com.example.gitstarscounter.stars

import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Star

@Suppress("DEPRECATION")
class StarsCallback(val presenter: StarsPresenter) {
    private var starsList: MutableList<Star> = mutableListOf()
    fun getStarsResponse(responseStarsList: List<Star?>?) {
        if (responseStarsList != null) {
            responseStarsList.forEach {
                starsList.add(it!!)
            }
        } else {
            presenter.showError(R.string.unknown_user_text)
        }
    }

    fun getError(t: Throwable) {
        presenter.showError(R.string.no_internet_text)
    }

    fun getStrasListSize(): Int {
        return starsList.size
    }

    fun getYearOfLastStar(): Int {
        if(starsList.isEmpty()) {
            return 0
        } else {
            return starsList[starsList.size - 1].starred_at.year
        }
    }

    fun loadGraph(error: Boolean) {
        if(!error) {
            presenter.loadGrafic(starsList)
        }
    }
}


