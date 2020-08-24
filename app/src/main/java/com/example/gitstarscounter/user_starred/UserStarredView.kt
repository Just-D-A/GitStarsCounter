package com.example.gitstarscounter.user_starred

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.gitstarscounter.git_api.Star
import com.example.gitstarscounter.git_api.User
import com.jjoe64.graphview.series.DataPoint


@StateStrategyType(value = AddToEndSingleStrategy::class)
interface UserStarredView : MvpView {
    fun startLoading()
    fun endLoading()
    fun setupUsersList(starList: MutableList<Star>)
    fun showError(textResource: Int) //другая стратегия
}