package com.example.gitstarscounter.stars

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.gitstarscounter.git_api.Star
import com.jjoe64.graphview.series.DataPoint

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface StarsView : MvpView {
    fun startLoading()
    fun endLoading()
    fun setupStarsGrafic(pointsList: ArrayList<DataPoint>)
    fun openUsersStared(starsInMonthList: MutableList<Star>)
    fun showError(textResource: Int) //другая стратегия
}