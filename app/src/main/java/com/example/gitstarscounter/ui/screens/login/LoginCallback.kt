package com.example.gitstarscounter.ui.screens.login

import com.example.gitstarscounter.data.remote.entity.ResourceRemote
import com.example.gitstarscounter.entity.RepositoryModel

interface LoginCallback {
    fun onLoginResponse(repositoryRemoteList: List<RepositoryModel>, noInternetIsVisible: Boolean)
    fun onError()
    fun onLimitedError()
    fun onUnknownUser(textResource: Int, noInternetIsVisible: Boolean)
    fun onLimitRemaining(resourceRemote: ResourceRemote)
}
