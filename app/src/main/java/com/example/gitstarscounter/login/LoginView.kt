package com.example.gitstarscounter.login

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.gitstarscounter.git_api.Repository

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface LoginView: MvpView {
    fun startLoading()
    fun endLoading()
    fun setupRepositoriesList(repositoriesList: List<Repository?>?)
    fun openStars(userName: String, repositoryName: String)
    @StateStrategyType(value = SkipStrategy::class)
    fun showError(textResource: Int) // другая стратегия
}