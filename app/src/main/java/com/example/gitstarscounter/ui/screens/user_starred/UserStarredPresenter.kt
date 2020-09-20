package com.example.gitstarscounter.ui.screens.user_starred

import com.example.gitstarscounter.data.to_rename_2.remote.entity.RemoteStar
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.omegar.mvp.InjectViewState

@InjectViewState
class UserStarredPresenter() : BasePresenter<UserStarredView>() {
    fun loadUserList(remoteStarList: MutableList<RemoteStar>) {
        viewState.setWaiting(true)
        viewState.setupUsersList(remoteStarList)
        viewState.setWaiting(false)
    }
}
