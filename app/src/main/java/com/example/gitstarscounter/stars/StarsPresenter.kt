package com.example.gitstarscounter.stars

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.Star
import com.jjoe64.graphview.series.DataPoint

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter() : MvpPresenter<StarsView>() {
    val starsConvector = StarsConvector
    val starsCallback = StarsCallback(this)
    val starsProvider = StarsProvider(starsCallback)
    var currYear: Int
    lateinit var userName: String
    lateinit var repository: Repository


    init {
        currYear = YEAR_IS_NOW
    }


    fun setParams(userName: String, repository: Repository) {
        this.userName = userName
        this.repository = repository
    }

    fun startLoadStars() {
        viewState.showSelectedYear(currYear.plus(1900), currYear < YEAR_IS_NOW)
        viewState.startLoading()

        starsProvider.loadStars(userName, repository, 1)
    }

    fun loadMoreStars(pageNumber: Int) {
        starsProvider.loadStars(userName, repository, pageNumber)
    }

    fun loadGrafic(starsList: List<Star?>?) {
        Log.d("CURR_YEAR", currYear.toString())
        starsConvector.setStarsMap(starsList, currYear)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()

        pointsList.forEach {
            Log.d("PINTS_COUNT", "${it.x} ${it.y}")
        }


        viewState.endLoading()
        viewState.setupStarsGrafic(pointsList, maxValueOfY.plus(1))
    }

    fun showError(textResource: Int) {
        viewState.endLoading()
        viewState.showError(textResource)
    }

    fun changeCurrentYear(more: Boolean) {
        if (more && (currYear + 1 <= YEAR_IS_NOW)) {
            currYear++
        } else if (!more) {
            currYear--
        }
        reloadStars()
    }

    fun reloadStars() {
        viewState.showSelectedYear(currYear.plus(1900), currYear < YEAR_IS_NOW)
        viewState.startLoading()
        val starsList = starsCallback.getStrasList()
        starsConvector.setStarsMap(starsList, currYear)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()
        viewState.endLoading()
        viewState.setupStarsGrafic(pointsList, maxValueOfY.plus(1))
    }

    fun openUserStarred(x: Double) {
        val starsInMonthList = starsConvector.getStarListByMonth(x.toInt())
        viewState.openUsersStared(starsInMonthList)
    }


    companion object {
        private const val YEAR_IS_NOW = 120 //java date need -1900
    }
}