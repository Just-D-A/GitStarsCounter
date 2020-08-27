package com.example.gitstarscounter.stars

import com.example.gitstarscounter.git_api.Star

interface StarsCallback {
    fun onStarsResponse(responseStarsList: List<Star>)

    fun onError(textResource: Int)

}


