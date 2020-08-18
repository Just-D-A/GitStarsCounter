package com.example.gitstarscounter.login

import android.os.Handler
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.R

@InjectViewState
class LoginPresenter: MvpPresenter<LoginView>() {
    fun testLoadStars(accountName: String) {
        Log.d("LoginPresenter", accountName)
        viewState.startLoading()
        Handler().postDelayed({ // исправить на Background Thread
           viewState.endLoading()
           if(accountName != "") {
               viewState.openStars()
           } else {
               viewState.showError(R.string.login_error)
           }
        }, 500)

    }



}