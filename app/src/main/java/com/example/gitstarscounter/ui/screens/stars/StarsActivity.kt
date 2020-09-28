package com.example.gitstarscounter.ui.screens.stars

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.presenter.InjectPresenter
import com.omegar.mvp.presenter.ProvidePresenter

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StarsActivity : BaseActivity(), StarsView {
    companion object {
        private const val USER_NAME_EXTRA = "userName"
        private const val REPOSITORY_EXTRA = "repository"

        //values for graph coordinates
        private const val MAX_X_VALUE = 12.5 //as month count + 0.5
        private const val MIN_X_VALUE = 0.0
        private const val MIN_Y_VALUE = 0.0
        private const val SERIES_SPACING = 50

        //values for colorful graph
        private const val MAX_COLOR_VALUE = 255
        private const val BLUE_VALUE = 100
        private const val FIRST_DIVIDER = 4
        private const val SECOND_DIVIDER = 6

        fun createLauncher(
            userName: String,
            repository: Repository
        ) =
            createActivityLauncher(
                USER_NAME_EXTRA put userName,
                REPOSITORY_EXTRA put repository
            )
    }

    private val graphGraphView: GraphView by bind(R.id.graph_view_stars)// need "by" under "by" formatting??
    private val databaseMessageTextView: TextView by bind(R.id.text_view_database_data_message_star)
    private val yearTextView: TextView by bind(R.id.text_view_selected_year)
    private val moreYearButton: Button by bind(R.id.button_more_year)
    private val lessYearButton: Button by bind(R.id.button_less_year)

    private var hasInternet = true

    @InjectPresenter
    override lateinit var presenter: StarsPresenter

    @ProvidePresenter
    fun provideDetailsPresenter(): StarsPresenter {
        return StarsPresenter(
            intent.getStringExtra(USER_NAME_EXTRA),
            intent.getSerializableExtra(REPOSITORY_EXTRA) as Repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stars)

        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        lessYearButton.setOnClickListener {
            presenter.responseToChangeCurrentYear(false)
        }

        moreYearButton.setOnClickListener {
            presenter.responseToChangeCurrentYear(true)
        }
    }

    override fun setupStarsGraph(pointsList: ArrayList<DataPoint>, maxValueOfY: Double) {
        val points = pointsList.toTypedArray()
        graphGraphView.removeAllSeries() // clear graph
        graphGraphView.viewport.setMinX(MIN_X_VALUE)
        graphGraphView.viewport.setMaxX(MAX_X_VALUE)
        graphGraphView.viewport.setMinY(MIN_Y_VALUE)
        graphGraphView.viewport.setMaxY(maxValueOfY)

        graphGraphView.viewport.isXAxisBoundsManual = true
        graphGraphView.viewport.isYAxisBoundsManual = true

        val series = BarGraphSeries(points)
        graphGraphView.addSeries(series)

        // styling
        series.setValueDependentColor { data ->
            Color.rgb(
                data.x.toInt() * MAX_COLOR_VALUE / FIRST_DIVIDER,
                Math.abs(data.y * MAX_COLOR_VALUE / SECOND_DIVIDER).toInt(), BLUE_VALUE
            )
        }

        series.spacing = SERIES_SPACING
        series.valuesOnTopColor = Color.RED

        //tap listener
        series.setOnDataPointTapListener { _, dataPoint ->
            presenter.responseToOpenUserStarred(dataPoint.x)
        }
        series.isDrawValuesOnTop = true
    }

    override fun changeVisibilityOfDataMessage(visible: Boolean) {
        hasInternet = visible
        databaseMessageTextView.isVisible = visible
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun showSelectedYear(selectedYear: Int, showMoreButton: Boolean) {
        yearTextView.text = selectedYear.toString()
        if (showMoreButton) {
            moreYearButton.isVisible = true
        } else {
            moreYearButton.visibility = View.INVISIBLE
        }
    }
}
