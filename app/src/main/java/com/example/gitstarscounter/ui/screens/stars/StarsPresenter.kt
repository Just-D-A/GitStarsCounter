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
import java.util.Collections.min
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter(val userName: String, val repository: Repository) :
    BasePresenter<StarsView>() {

    private var currYear = Calendar.getInstance().get(Calendar.YEAR)
    private var starsList = mutableListOf<Star>()

    @Inject
    lateinit var starRepository: StarRepository

    init {
        GitStarsApplication.instance.gitStarsCounterComponent.inject(this)
        startLoadStars()
    }

    private fun startLoadStars() {
        starsList.clear()

        viewState.showSelectedYear(currYear, currYear < Calendar.getInstance().get(Calendar.YEAR))

        launchWithWaiting {
            val responseStarList =
                starRepository.getRepositoryStars(userName, repository)
            starsList.addAll(responseStarList)
            loadGraph()
        }
    }

    private fun loadGraph() {
        val starsConvector = StarsConvector(starsList, currYear)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()

        viewState.setupStarsGraph(pointsList, maxValueOfY + 1)
    }

    fun requestToChangeCurrentYear(more: Boolean) {
        if (more) {
            currYear = kotlin.math.min(currYear + 1, Calendar.getInstance().get(Calendar.YEAR))
        } else {
            currYear--
        }
        reloadStars()
    }

    private fun reloadStars() {
        viewState.showSelectedYear(currYear, currYear < Calendar.getInstance().get(Calendar.YEAR))
        val starsConvector = StarsConvector(starsList, currYear)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()
        viewState.setupStarsGraph(pointsList, maxValueOfY + 1)
    }

    fun requestToOpenUserStarred(x: Double) {
        val starsInMonthList = StarsConvector(starsList, currYear).getStarListByMonth(x.toInt())
        UserStarredActivity.createLauncher(starsInMonthList).launch()
    }
}
