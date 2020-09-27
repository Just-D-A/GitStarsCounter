package com.example.gitstarscounter.ui.screens.user_starred

import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.omegar.mvp.InjectViewState

@InjectViewState
class UserStarredPresenter : BasePresenter<UserStarredView>() {
    fun loadUserList(remoteStarList: MutableList<Star>) {
        viewState.setWaiting(true)
        viewState.setupUsersList(remoteStarList)
        viewState.setWaiting(false)
    }
}
