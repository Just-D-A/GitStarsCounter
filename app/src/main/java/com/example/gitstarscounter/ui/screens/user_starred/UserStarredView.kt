package com.example.gitstarscounter.ui.screens.user_starred

import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.ui.screens.base.BaseView
import com.omega_r.base.mvp.strategies.RemoveEndTagStrategy
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType

interface UserStarredView : BaseView {
    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setupUsersList(remoteStarList: MutableList<Star>)

    @StateStrategyType(RemoveEndTagStrategy::class)
    override fun hideQueryOrMessage()
}
