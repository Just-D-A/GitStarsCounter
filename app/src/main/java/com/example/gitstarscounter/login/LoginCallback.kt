package com.example.gitstarscounter.login

import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Repository

interface LoginCallback {
    fun onLoginResponse(repositoryList: List<Repository?>?) /*{
        if(repositoryList.isNullOrEmpty()) {
            presenter.showError(R.string.unknown_user_text)
        } else {
            presenter.getRepositories(repositoryList)
        }
    }*/

    fun onError(textResource: Int) /*{
        presenter.showError(R.string.no_internet_text)
    }*/
}