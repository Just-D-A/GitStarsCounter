package com.example.gitstarscounter.stars

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.StarModel
import com.example.gitstarscounter.user_starred.UserStarredActivity
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StarsActivity : MvpAppCompatActivity(), StarsView {

    private lateinit var waitProgressView: CircularProgressView
    private lateinit var graphGraphView: GraphView
    private lateinit var yearTextView: TextView
    private lateinit var moreYearButton: Button
    private lateinit var lessYearButton: Button
    private lateinit var noInternetTextView: TextView
    private lateinit var limitedTextView: TextView

    private var hasInternet = true

    @InjectPresenter
    lateinit var starsPresenter: StarsPresenter

    companion object {
        const val BACK_BUTTON_ID = 16908332

        private const val KEY_USER_NAME = "userName"
        private const val KEY_REPOSITORY = "repository"
        private const val KEY_LIMIT_RESOURCE_COUNT = "limitResourceCount"

        fun createLauncher(
            userName: String,
            repositoryModel: RepositoryModel,
            limitResourceCount: Int
        ) =
            createActivityLauncher(
                KEY_USER_NAME put userName,
                KEY_REPOSITORY put repositoryModel,
                KEY_LIMIT_RESOURCE_COUNT put limitResourceCount
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stars)
        starsPresenter.setParams(
            intent.getStringExtra(KEY_USER_NAME),
            intent.getSerializableExtra(KEY_REPOSITORY) as RepositoryModel,
            intent.getIntExtra(KEY_LIMIT_RESOURCE_COUNT, 0)
        )
        starsPresenter.startLoadStars() ///////////^^^^/////////////////////////////////////////////////////ЧТО С ЭТИМ ДЕЛАТЬ
        graphGraphView = findViewById(R.id.graph_view_stars)
        yearTextView = findViewById(R.id.text_view_selected_year)
        lessYearButton = findViewById(R.id.button_less_year)
        moreYearButton = findViewById(R.id.button_more_year)
        waitProgressView = findViewById(R.id.progress_view_stars)
        noInternetTextView = findViewById(R.id.text_view_no_internet_stars)
        limitedTextView = findViewById(R.id.text_view_limited_resource_stars)

        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true);
        actionBar?.setDisplayHomeAsUpEnabled(true);

        lessYearButton.setOnClickListener {
            starsPresenter.changeCurrentYear(false)
        }

        moreYearButton.setOnClickListener {
            starsPresenter.changeCurrentYear(true)
        }
    }

    override fun showError(textResource: Int) {
        Toast.makeText(applicationContext, textResource, Toast.LENGTH_SHORT).show()
    }

    override fun changeVisibilityOfLimitedView(visible: Boolean) {
        limitedTextView.isVisible = visible
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
            starsPresenter.openUserStarred(dataPoint.x)
        }
        series.isDrawValuesOnTop = true
    }

    override fun changeVisibilityOfNoInternetView(visible: Boolean) {
        hasInternet = visible
        noInternetTextView.isVisible = visible
    }

    override fun openUsersStared(starsInMonthList: MutableList<StarModel>) {
        UserStarredActivity.createLauncher(starsInMonthList, hasInternet).launch(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.home -> {
                //Log.d("BackButton", "pressed")
                val limitResourceCount = starsPresenter.getLimitResourceCount()
                val intent = Intent()
                intent.putExtra("limitResourceCount", limitResourceCount)
                setResult(RESULT_OK, intent)
                this.finish()
                true
            }

            BACK_BUTTON_ID -> {
                //Log.d("BackButton", "pressed by num id")
                val limitResourceCount = starsPresenter.getLimitResourceCount()
                val intent = Intent()
                intent.putExtra("limitResourceCount", limitResourceCount)
                setResult(RESULT_OK, intent)
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

    override fun startLoading() {
        waitProgressView.isVisible = true
        graphGraphView.isVisible = false
    }

    override fun endLoading() {
        waitProgressView.isVisible = false
        graphGraphView.isVisible = true
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Log.d("Stars", "onUserLeaveHint")
    }
}
