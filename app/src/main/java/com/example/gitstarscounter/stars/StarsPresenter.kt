package com.example.gitstarscounter.stars

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.git_api.Star
import com.jjoe64.graphview.series.DataPoint

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter : MvpPresenter<StarsView>() {

    fun startLoadStars(userName: String, repositoryName: String) {
        viewState.startLoading()
        StarsProvider(this).loadStars(userName, repositoryName)
    }

    fun loadGrafic(starsList: List<Star?>?) {
        val starsConvector = StarsConvector(starsList)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        Log.d("STAR ", pointsList.size.toString())


        viewState.endLoading()
        viewState.setupStarsGrafic(pointsList)
    }

    fun showError(textResource: Int) {
        viewState.endLoading()
        viewState.showError(textResource)
    }
}