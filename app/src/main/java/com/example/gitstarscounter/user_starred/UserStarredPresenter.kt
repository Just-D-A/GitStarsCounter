package com.example.gitstarscounter.user_starred

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.git_api.StarModel

@InjectViewState
class UserStarredPresenter() : MvpPresenter<UserStarredView>() {

    fun loadUserList(starModelList: MutableList<StarModel>) {
        viewState.startLoading()
        viewState.setupUsersList(starModelList)
        viewState.endLoading()
    }
}