package com.example.gitstarscounter.stars

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.gitstarscounter.git_api.StarModel
import com.jjoe64.graphview.series.DataPoint

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface StarsView : MvpView {
    fun startLoading()
    fun endLoading()
    fun setupStarsGrafic(pointsList: ArrayList<DataPoint>, maxValueOfY: Double)
    fun openUsersStared(starsInMonthList: MutableList<StarModel>)
    fun showSelectedYear(selectedYear: Int, showMoreButton: Boolean)
    fun changeVisibilityOfNoInternetView(visible: Boolean)

    @StateStrategyType(value = SkipStrategy::class)
    fun showError(textResource: Int) //другая стратегия
}
