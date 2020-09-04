package com.example.gitstarscounter.login

import com.example.gitstarscounter.git_api.RepositoryModel

interface LoginCallback {
    fun onLoginResponse(repositoryModelList: List<RepositoryModel>, noInternetIsVisible: Boolean)
    fun onError(textResource: Int)
}