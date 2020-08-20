package com.example.gitstarscounter.login

import com.example.gitstarscounter.retrofit2.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginProvider(var presenter: LoginPresenter) {
    fun loadUser(userName: String) {
        //bacgroundThread
        //retrofit
        val repository = SearchRepositoryProvider.provideSearchRepository()
        repository.getUser(userName)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ result ->
                presenter.getUser(result)
            }, { error ->
                presenter.showError(error as Exception)
                // error.printStackTrace()
            })
    }
}