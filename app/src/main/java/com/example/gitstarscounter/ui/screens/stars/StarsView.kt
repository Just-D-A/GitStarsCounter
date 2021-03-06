package com.example.gitstarscounter.ui.screens.stars

import com.example.gitstarscounter.ui.screens.base.BaseView
import com.jjoe64.graphview.series.DataPoint
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType

interface StarsView : BaseView {
    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setupStarsGraph(pointsList: ArrayList<DataPoint>, maxValueOfY: Double)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showSelectedYear(selectedYear: Int, showMoreButton: Boolean)
}
