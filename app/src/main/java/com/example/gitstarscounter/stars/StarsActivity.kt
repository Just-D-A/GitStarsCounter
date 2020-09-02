package com.example.gitstarscounter.stars

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.Star
import com.example.gitstarscounter.user_starred.UserStarredActivity
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import java.io.Serializable


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StarsActivity : MvpAppCompatActivity(), StarsView {

    private lateinit var waitProgressView: CircularProgressView
    private lateinit var graphGraphView: GraphView
    private lateinit var yearTextView: TextView
    private lateinit var moreYearButton: Button
    private lateinit var lessYearButton: Button
    private lateinit var noInternetTextView: TextView
    private var hasInternet = true

    @InjectPresenter
    lateinit var starsPresenter: StarsPresenter

    companion object {

        private const val KEY_USER_NAME = "userName"
        private const val KEY_REPOSITORY = "repository"


        fun createIntent(context: Context, userName: String, repository: Repository) = Intent(
            context,
            StarsActivity::class.java
        )
            .putExtra(KEY_USER_NAME, userName)
            .putExtra(KEY_REPOSITORY, repository as Serializable)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stars)
        starsPresenter.setParams(
            intent.getStringExtra(KEY_USER_NAME),
            intent.getSerializableExtra(KEY_REPOSITORY) as Repository
        )
        starsPresenter.startLoadStars() ///////////^^^^/////////////////////////////////////////////////////ЧТО С ЭТИМ ДЕЛАТЬ
        graphGraphView = findViewById(R.id.graph_view_stars)
        yearTextView = findViewById(R.id.text_view_selected_year)
        lessYearButton = findViewById(R.id.button_less_year)
        moreYearButton = findViewById(R.id.button_more_year)
        noInternetTextView = findViewById(R.id.text_view_no_internet_stars)

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

    override fun setupStarsGrafic(pointsList: ArrayList<DataPoint>, maxValueOfY: Double) {

        graphGraphView.removeAllSeries()
        val points = pointsList.toTypedArray()
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

        //tap listner
        series.setOnDataPointTapListener { _, dataPoint ->
            starsPresenter.openUserStarred(dataPoint.x)
        }
        series.isDrawValuesOnTop = true
    }

    override fun changeVisibilityOfNoInternetView(visible: Boolean) {
        hasInternet = visible
        noInternetTextView.isVisible = visible
    }

    override fun openUsersStared(starsInMonthList: MutableList<Star>) {
        startActivity(UserStarredActivity.createIntent(this, starsInMonthList, hasInternet))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.home -> {
                //Log.d("BackButton", "pressed")
                this.finish()
                true
            }

            16908332 -> {
                //Log.d("BackButton", "pressed by num id")
                this.finish()
                true
            }

            else -> {
                //Log.d("BackButtonElse", "${item.itemId} == ${R.id.home}")
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

}
