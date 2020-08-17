package com.example.gitstarscounter.presenters

import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.models.StarModel
import com.example.gitstarscounter.views.StarsView

class StarsPresenter: MvpPresenter<StarsView>() {
    fun loadStars() {
        viewState.startLoading()
    }

    fun loadGrafic(starsList: ArrayList<StarModel>) {
        //сделать график с помощью библиотеки
        viewState.endLoading()
    }
}