package com.example.gitstarscounter.ui.screens.user_starred

import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.omegar.mvp.InjectViewState

@InjectViewState
class UserStarredPresenter(private val remoteStarList: MutableList<Star>) :
    BasePresenter<UserStarredView>() {
    init {
        loadUserList()
    }

    fun loadUserList() {
        viewState.setupUsersList(remoteStarList)
    }
}
