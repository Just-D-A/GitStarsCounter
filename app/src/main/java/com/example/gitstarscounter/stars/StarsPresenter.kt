package com.example.gitstarscounter.stars

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.retrofit2.Star
import com.jjoe64.graphview.series.DataPoint

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter : MvpPresenter<StarsView>() {

    fun startLoadStars(userName: String) {
        viewState.startLoading()
        StarsProvider(this).loadStars(userName)
    }

    fun loadGrafic(starsList: List<Star?>?) {
        val starsConvector = StarsConvector(starsList)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()

        viewState.endLoading()
        viewState.setupStarsGrafic(pointsList)
    }

    fun showError(error: Exception) {
        viewState.endLoading()
        viewState.showError(R.string.login_error)
    }
}