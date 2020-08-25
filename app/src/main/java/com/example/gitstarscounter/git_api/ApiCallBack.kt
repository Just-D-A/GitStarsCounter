package com.example.gitstarscounter.git_api

interface ApiCallback<T> {
   /* fun onException(error: Throwable)

    fun onError(error: String)*/

    fun onSuccess(t: T)
}
