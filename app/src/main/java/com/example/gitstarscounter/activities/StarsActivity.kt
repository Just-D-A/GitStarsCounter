package com.example.gitstarscounter.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.models.StarModel
import com.example.gitstarscounter.presenters.StarsPresenter
import com.example.gitstarscounter.views.StarsView
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class StarsActivity : MvpAppCompatActivity(), StarsView {

    private lateinit var mCpvWait: CircularProgressView
    private lateinit var mGraph: GraphView

    @InjectPresenter
    lateinit var starsPresenter: StarsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stars)

        mGraph = findViewById(R.id.graph)
        val series: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>(
            arrayOf<DataPoint>(
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
            )
        )
        mGraph.addSeries(series)
    }

    override fun showError(textResource: Int) {
        Toast.makeText(applicationContext, textResource, Toast.LENGTH_SHORT).show()
    }

    override fun setupStarsList(friendsList: ArrayList<StarModel>) {

    }

    override fun startLoading() {
        mGraph.visibility = View.GONE
        mCpvWait.visibility = View.VISIBLE
    }

    override fun endLoading() {
        mGraph.visibility = View.VISIBLE
        mCpvWait.visibility = View.GONE
    }

}