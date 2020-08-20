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
    private lateinit var grafGraphView: GraphView

    @InjectPresenter
    lateinit var starsPresenter: StarsPresenter

    companion object {

        private const val KEY_VALUE = "value"
        var BaseUrl = "http://api.github.com"
        var AppId = "2e65127e909e178d0af311a81f39948c"
        var lat = "35"
        var lon = "139"

        fun createIntent(context: Context, values: String) = Intent(context, StarsActivity::class.java)
            .putExtra(KEY_VALUE, values)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stars)

        intent.getStringExtra(KEY_VALUE)

        waitProgressView = findViewById(R.id.progress_view_stars)
        grafGraphView = findViewById(R.id.graph_view_stars)
    }

    override fun showError(textResource: Int) {
        Toast.makeText(applicationContext, textResource, Toast.LENGTH_SHORT).show()
    }

    override fun setupStarsGrafic(pointsList: ArrayList<DataPoint>) {
        grafGraphView = findViewById(R.id.graph_view_stars)
        val points = pointsList.toTypedArray()
        val series  = LineGraphSeries(points)
        grafGraphView.addSeries(series)
    }

    override fun startLoading() {
        waitProgressView.isVisible = true
        grafGraphView.isVisible = false

    }

    override fun endLoading() {
        waitProgressView.isVisible = false
        grafGraphView.isVisible = true
    }

}