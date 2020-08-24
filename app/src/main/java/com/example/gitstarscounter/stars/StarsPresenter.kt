package com.example.gitstarscounter.stars

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.Star
import com.jjoe64.graphview.series.DataPoint

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter() : MvpPresenter<StarsView>() {
    private val starsConvector = StarsConvector
    private var currYear: Int
    private lateinit var userName: String
    private lateinit var repository: Repository

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

        StarsProvider(this).loadStars(userName, repository)
    }

    fun loadGrafic(starsList: List<Star?>?) {
        starsConvector.setStarsMap(starsList, currYear)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()

        viewState.endLoading()
        viewState.setupStarsGrafic(pointsList, maxValueOfY.plus(1))
    }

    fun showError(textResource: Int) {
        viewState.endLoading()
        viewState.showError(textResource)
    }

    fun changeCurrentYear(more: Boolean) {
        if(more && (currYear+1 <= YEAR_IS_NOW)) {
            currYear++
        } else if(!more) {
            currYear--
        }
        startLoadStars()
    }

    fun openUserStarred(x: Double) {
        val starsInMonthList = starsConvector.getStarListByMonth(x.toInt())
        viewState.openUsersStared(starsInMonthList)
    }



    companion object {
        private const val YEAR_IS_NOW = 120 //java date need -1900
    }
}