package com.example.gitstarscounter.stars

import com.example.gitstarscounter.git_api.StarModel

interface StarsCallback {
    fun onStarsResponse(responseStarsList: List<StarModel>, noInternerIsVisible: Boolean)
    fun onError(textResource: Int)
}


