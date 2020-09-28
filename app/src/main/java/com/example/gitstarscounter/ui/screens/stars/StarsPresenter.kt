package com.example.gitstarscounter.ui.screens.stars

import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.providers.star.StarRepository
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.example.gitstarscounter.ui.screens.user_starred.UserStarredActivity
import com.jjoe64.graphview.series.DataPoint
import com.omegar.mvp.InjectViewState
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter(val userName: String, val repository: Repository) :
    BasePresenter<StarsView>() {
    companion object {
        private val YEAR_IS_NOW = Calendar.getInstance().get(Calendar.YEAR)
    }

    private val starsConvector = StarsConvector
    private var currYear = YEAR_IS_NOW
    private var starsList = mutableListOf<Star>()

    @Inject
    lateinit var starRepository: StarRepository

    init {
        GitStarsApplication.instance.gitStarsCounterComponent.inject(this)
        startLoadStars()
    }

    private fun startLoadStars() {
        starsList.clear()

        viewState.showSelectedYear(currYear, currYear < YEAR_IS_NOW)

        launchWithWaiting {
            val responseStarList =
                starRepository.getRepositoryStars(userName, repository)
            starsList.addAll(responseStarList)
            loadGraph()
        }
    }

    private fun loadGraph() {
        starsConvector.setStarsMap(
            starsList,
            currYear
        )
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()

        viewState.setupStarsGraph(pointsList, maxValueOfY + 1)
    }

    fun responseToChangeCurrentYear(more: Boolean) {
        if (more && (currYear + 1 <= YEAR_IS_NOW)) {
            currYear++
        } else if (!more) {
            currYear--
        }
        reloadStars()
    }

    private fun reloadStars() {
        viewState.showSelectedYear(currYear, currYear < YEAR_IS_NOW)
        starsConvector.setStarsMap(
            starsList,
            currYear
        )
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()
        viewState.setupStarsGraph(pointsList, maxValueOfY + 1)
    }

    fun responseToOpenUserStarred(x: Double) {
        val starsInMonthList = starsConvector.getStarListByMonth(x.toInt())
        UserStarredActivity.createLauncher(starsInMonthList).launch()
    }
}
