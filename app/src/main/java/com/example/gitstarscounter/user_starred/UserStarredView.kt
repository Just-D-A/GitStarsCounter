package com.example.gitstarscounter.user_starred

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.gitstarscounter.git_api.StarModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface UserStarredView : MvpView {
    fun startLoading()
    fun endLoading()
    fun setupUsersList(starModelList: MutableList<StarModel>)

    @StateStrategyType(value = SkipStrategy::class)
    fun showError(textResource: Int) //другая стратегия
}
