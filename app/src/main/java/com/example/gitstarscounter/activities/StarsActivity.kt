package com.example.gitstarscounter.activities

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.gitstarscounter.R
import com.example.gitstarscounter.models.StarModel
import com.example.gitstarscounter.views.StarsView

class StarsActivity : MvpAppCompatActivity(), StarsView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stars)
    }

    override fun showError(textResource: Int) {

    }

    override fun setupStarsList(friendsList: ArrayList<StarModel>) {

    }

    override fun startLoading() {

    }

    override fun endLoading() {

    }

}