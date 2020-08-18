package com.example.gitstarscounter.stars

import com.arellomobile.mvp.MvpPresenter

class StarsPresenter: MvpPresenter<StarsView>() {
    fun loadStars() {
        viewState.startLoading()
    }

    fun loadGrafic(starsList: ArrayList<StarModel>) {
        //сделать график с помощью библиотеки
        viewState.endLoading()
    }
}