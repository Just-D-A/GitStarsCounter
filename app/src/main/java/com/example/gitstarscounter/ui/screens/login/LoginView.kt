package com.example.gitstarscounter.ui.screens.login

import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BaseView
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType

interface LoginView : BaseView {
    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setupRepositoriesList(repositoriesList: List<Repository>)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun addRepositoriesToList(repositoriesList: List<Repository>)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun endPagination()
}
