package com.example.gitstarscounter.ui.screens.stars

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.remote.entity.RepositoryRemote
import com.example.gitstarscounter.data.remote.entity.StarRemote
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.example.gitstarscounter.ui.screens.user_starred.UserStarredActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.presenter.InjectPresenter

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StarsActivity : BaseActivity(), StarsView {

    private lateinit var graphGraphView: GraphView
    private lateinit var yearTextView: TextView
    private lateinit var moreYearButton: Button
    private lateinit var lessYearButton: Button
    private lateinit var noInternetTextView: TextView

    private var hasInternet = true

    @InjectPresenter
    override lateinit var presenter: StarsPresenter

    companion object {
        const val BACK_BUTTON_ID = 16908332

        private const val KEY_USER_NAME = "userName"
        private const val KEY_REPOSITORY = "repository"
        private const val KEY_LIMIT_RESOURCE_COUNT = "limitResourceCount"

        fun createLauncher(
            userName: String,
            repositoryRemote: RepositoryModel,
            limitResourceCount: Int
        ) =
            createActivityLauncher(
                KEY_USER_NAME put userName,
                KEY_REPOSITORY put repositoryRemote as RepositoryRemote,
                KEY_LIMIT_RESOURCE_COUNT put limitResourceCount
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stars)
        presenter.setParams(
            intent.getStringExtra(KEY_USER_NAME),
            intent.getSerializableExtra(KEY_REPOSITORY) as RepositoryModel
        )

        presenter.responseToStartLoadStars()

        graphGraphView = findViewById(R.id.graph_view_stars)!!
        yearTextView = findViewById(R.id.text_view_selected_year)!!
        lessYearButton = findViewById(R.id.button_less_year)!!
        moreYearButton = findViewById(R.id.button_more_year)!!
        noInternetTextView = findViewById(R.id.text_view_database_data_message)!!

        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true);
        actionBar?.setDisplayHomeAsUpEnabled(true);

        lessYearButton.setOnClickListener {
            presenter.responseToChangeCurrentYear(false)
        }

        moreYearButton.setOnClickListener {
            presenter.responseToChangeCurrentYear(true)
        }
    }

    override fun setupStarsGrafic(pointsList: ArrayList<DataPoint>, maxValueOfY: Double) {
        val points = pointsList.toTypedArray()
        graphGraphView.removeAllSeries() // clear graph
        graphGraphView.viewport.setMinX(0.0)
        graphGraphView.viewport.setMaxX(12.5)
        graphGraphView.viewport.setMinY(0.0)
        graphGraphView.viewport.setMaxY(maxValueOfY)

        graphGraphView.viewport.isXAxisBoundsManual = true
        graphGraphView.viewport.isYAxisBoundsManual = true
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

        //tap listener
        series.setOnDataPointTapListener { _, dataPoint ->
            presenter.responseToOpenUserStarred(dataPoint.x)
        }
        series.isDrawValuesOnTop = true
    }

    override fun changeVisibilityOfDataMessage(visible: Boolean) {
        hasInternet = visible
        noInternetTextView.isVisible = visible
    }

    override fun openUsersStared(starsInMonthList: MutableList<StarRemote>) {
        UserStarredActivity.createLauncher(starsInMonthList, hasInternet).launch(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.home -> {
                this.finish()
                true
            }

            BACK_BUTTON_ID -> {
                this.finish()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
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

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Log.d("Stars", "onUserLeaveHint")
    }
}
