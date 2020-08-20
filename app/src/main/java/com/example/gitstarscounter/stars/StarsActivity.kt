package com.example.gitstarscounter.stars

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.gitstarscounter.R
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.OnDataPointTapListener

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StarsActivity : MvpAppCompatActivity(),
    StarsView {

    private lateinit var waitProgressView: CircularProgressView
    private lateinit var graphGraphView: GraphView

    @InjectPresenter
    lateinit var starsPresenter: StarsPresenter

    companion object {

        private const val KEY_VALUE = "userName"

        fun createIntent(context: Context, userName: String) = Intent(
            context,
            StarsActivity::class.java
        )
            .putExtra(KEY_VALUE, userName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stars)

        starsPresenter.startLoadStars(intent.getStringExtra(KEY_VALUE))

        waitProgressView = findViewById(R.id.progress_view_stars)
        graphGraphView = findViewById(R.id.graph_view_stars)
    }

    override fun showError(textResource: Int) {
        Toast.makeText(applicationContext, textResource, Toast.LENGTH_SHORT).show()
    }

    override fun setupStarsGrafic(pointsList: ArrayList<DataPoint>) {

        val points = pointsList.toTypedArray()
        graphGraphView.viewport.setMinX(0.0);
        graphGraphView.viewport.setMaxX(12.5);
        graphGraphView.viewport.isXAxisBoundsManual = true;
        val series = BarGraphSeries(points)
        graphGraphView.addSeries(series)

// styling
        series.setValueDependentColor { data ->
            Color.rgb(
                data.x.toInt() * 255 / 4,
                Math.abs(data.y * 255 / 6).toInt(), 100
            )
        }

        series.spacing = 50
        series.valuesOnTopColor = Color.RED

        series.setOnDataPointTapListener(OnDataPointTapListener { _, dataPoint ->
            Toast.makeText(this, "Series1: On Data Point clicked: $dataPoint", Toast.LENGTH_SHORT)
                .show()
        })
        series.isDrawValuesOnTop = true
    }


    override fun startLoading() {
        waitProgressView.isVisible = true
        graphGraphView.isVisible = false

    }

    override fun endLoading() {
        waitProgressView.isVisible = false
        graphGraphView.isVisible = true
    }

}
