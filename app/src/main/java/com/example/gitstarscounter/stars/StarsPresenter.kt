package com.example.gitstarscounter.stars

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.Star
import com.jjoe64.graphview.series.DataPoint

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter() : MvpPresenter<StarsView>(), StarsCallback {
    private val starsConvector = StarsConvector
    private val starsProvider = StarsProvider()
    private var currYear: Int
    private var starsList: MutableList<Star> = mutableListOf()
    private var error = false
    private var pageNumber = 1
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

        starsProvider.loadStars(userName, repository, pageNumber, this)
    }

    fun loadMoreStars(pageNumber: Int) {
        starsProvider.loadStars(userName, repository, pageNumber, this)
    }

    fun loadGrafic() {
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

    override fun onStarsResponse(responseStarsList: List<Star?>?) {
        if (responseStarsList != null) {
            responseStarsList.forEach {
                Log.d("StarsCallback", it?.user?.login)
                starsList.add(it!!)
            }
            needMore()
            //presenter.loadGrafic(responseStarsList)
        } else {
            showError(R.string.unknown_user_text)
            error = true
        }
    }

    override fun onError(textResource: Int) {
        showError(textResource)
    }

    private fun needMore() {
        var lastStarYear = 0
        if (starsList.size != 0) {
            lastStarYear = starsList[starsList.size - 1].starred_at.year
        }
        val currStarsCount = starsList.size
        val allStarsCount = repository.allStarsCount
        if ((lastStarYear <= currYear) && (currStarsCount < allStarsCount) && (!error)) {
            pageNumber++
            loadMoreStars(pageNumber)
        } else if (!error) {
            loadGrafic()
        }
    }
}