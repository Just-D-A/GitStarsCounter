package com.example.gitstarscounter.login

import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Repository

interface LoginCallback {
    fun onLoginResponse(repositoryList: List<Repository>, noInternerIsVisible: Boolean)

    fun onError(textResource: Int)
}