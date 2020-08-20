package com.example.gitstarscounter.stars

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.jjoe64.graphview.series.DataPoint

@InjectViewState
class StarsPresenter: MvpPresenter<StarsView>() {

    init {
        startLoadStars()
    }

    fun startLoadStars() {
        viewState.startLoading()
        StarsProvider(this).testLoadStars()
    }

    fun loadGrafic(starsList: ArrayList<StarModel>) {
        val starsConvector = StarsConvector(starsList)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        viewState.endLoading()
        viewState.setupStarsGrafic(pointsList)
    }
}