package com.example.gitstarscounter.user_starred

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.git_api.Star

@InjectViewState
class UserStarredPresenter() : MvpPresenter<UserStarredView>() {

    fun loadUserList(starList: MutableList<Star>) {
        viewState.startLoading()
        viewState.setupUsersList(starList)
        viewState.endLoading()
    }
}