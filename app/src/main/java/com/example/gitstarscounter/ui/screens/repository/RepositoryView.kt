package com.example.gitstarscounter.ui.screens.repository

import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BaseView
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType

interface RepositoryView : BaseView {
    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setRepositoryList(repositoryList: List<Repository>)
}
