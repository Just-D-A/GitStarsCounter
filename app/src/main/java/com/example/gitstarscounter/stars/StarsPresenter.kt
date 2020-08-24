package com.example.gitstarscounter.stars

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.git_api.Star
import com.jjoe64.graphview.series.DataPoint

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter : MvpPresenter<StarsView>() {
    private val starsConvector = StarsConvector

    fun startLoadStars(userName: String, repositoryName: String) {
        viewState.startLoading()
        StarsProvider(this).loadStars(userName, repositoryName)
    }

    fun loadGrafic(starsList: List<Star?>?) {
        starsConvector.setStarsMap(starsList)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()


        viewState.endLoading()
        viewState.setupStarsGrafic(pointsList, maxValueOfY.plus(1))
    }

    fun showError(textResource: Int) {
        viewState.endLoading()
        viewState.showError(textResource)
    }

    fun openUserStarred(x: Double) {
        val starsInMonthList = starsConvector.getStarListByMonth(x.toInt())
        viewState.openUsersStared(starsInMonthList)
    }
}