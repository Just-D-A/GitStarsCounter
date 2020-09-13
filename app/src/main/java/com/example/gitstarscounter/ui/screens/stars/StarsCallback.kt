package com.example.gitstarscounter.ui.screens.stars

import com.example.gitstarscounter.entity.StarModel

interface StarsCallback {
    fun onStarsResponse(responseStarsList: List<StarModel>, noInternetIsVisible: Boolean)
    fun onError(textResource: Int)
}
