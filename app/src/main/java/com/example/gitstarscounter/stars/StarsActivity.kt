package com.example.gitstarscounter.stars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.gitstarscounter.R
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class StarsActivity : MvpAppCompatActivity(),
    StarsView {

    private lateinit var waitProgressView: CircularProgressView
    private lateinit var graphView: GraphView

    @InjectPresenter
    lateinit var starsPresenter: StarsPresenter

    companion object {

        private const val KEY_VALUE = "value"

        fun createIntent(context: Context, valus: String) = Intent(context, StarsActivity::class.java)
            .putExtra(KEY_VALUE, valus)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stars)

        //intent.getStringExtra(KEY_VALUE)

        graphView = findViewById(R.id.graph)
        val series: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>(
            arrayOf<DataPoint>(
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
            )
        )
        graphView.addSeries(series)
    }

    override fun showError(textResource: Int) {
        Toast.makeText(applicationContext, textResource, Toast.LENGTH_SHORT).show()
    }

    override fun setupStarsList(friendsList: ArrayList<StarModel>) {

    }

    override fun startLoading() {
        waitProgressView.isVisible = true
        graphView.isVisible = false

    }

    override fun endLoading() {
        waitProgressView.isVisible = false
        graphView.isVisible = true
    }

}