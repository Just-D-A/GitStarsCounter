package com.example.gitstarscounter.login

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import com.example.gitstarscounter.git_api.RepositoryModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface LoginView : MvpView {
    fun startLoading()
    fun endLoading()
    fun setupRepositoriesList(repositoriesList: List<RepositoryModel?>?)
    fun openStars(userName: String, repository: RepositoryModel)
    fun changeVisibilityOfNoInternetView(visible: Boolean)
    fun addPagination(repositoriesList: List<RepositoryModel>)
    fun endPagination()

    @StateStrategyType(value = SkipStrategy::class)
    fun showError(textResource: Int) // другая стратегия
}
