package com.example.gitstarscounter.stars

import com.arellomobile.mvp.MvpView

interface StarsView: MvpView {
    fun showError(textResource: Int)
    fun setupStarsList(friendsList: ArrayList<StarModel>)
    fun startLoading()
    fun endLoading()
}