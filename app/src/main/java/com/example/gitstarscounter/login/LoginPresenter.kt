package com.example.gitstarscounter.login

import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.retrofit2.User

@InjectViewState
class LoginPresenter: MvpPresenter<LoginView>() {
    fun testLoadStars(userName: String) {
        viewState.startLoading()
        Handler().postDelayed({ // исправить на Background Thread
           viewState.endLoading()
           if(userName != "") {
               viewState.openStars()
           } else {
               viewState.showError(R.string.login_error)
           }
        }, 500)

    }

    fun loadUser(userName: String) {
        viewState.startLoading()
        LoginProvider(this).loadUser(userName)
    }

    fun getUser(user: User?) {
        viewState.endLoading()
        //get reps of this user
        if(user != null) {
            viewState.openStars()
        } else {
            viewState.showError(R.string.login_error)
        }
    }

    fun showError(error: Exception){
        viewState.endLoading()
        viewState.showError(R.string.login_error)
    }



}