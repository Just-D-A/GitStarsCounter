package com.example.gitstarscounter.views

import com.arellomobile.mvp.MvpView
import com.example.gitstarscounter.models.StarModel

interface StarsView: MvpView {
    fun showError(textResource: Int)
    fun setupStarsList(friendsList: ArrayList<StarModel>)
    fun startLoading()
    fun endLoading()
}