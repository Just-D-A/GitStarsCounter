package com.example.gitstarscounter.stars

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.jjoe64.graphview.series.DataPoint

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface StarsView: MvpView {
    fun showError(textResource: Int)
    fun setupStarsGrafic(pointsList: ArrayList<DataPoint>)
    fun startLoading()
    fun endLoading()
}