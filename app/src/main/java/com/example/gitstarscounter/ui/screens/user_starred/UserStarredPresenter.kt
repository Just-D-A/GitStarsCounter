package com.example.gitstarscounter.ui.screens.user_starred

import com.example.gitstarscounter.data.remote.entity.StarRemote
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.omegar.mvp.InjectViewState

@InjectViewState
class UserStarredPresenter() : BasePresenter<UserStarredView>() {
    fun loadUserList(starRemoteList: MutableList<StarRemote>) {
        viewState.setWaiting(true)
        viewState.setupUsersList(starRemoteList)
        viewState.setWaiting(false)
    }
}
