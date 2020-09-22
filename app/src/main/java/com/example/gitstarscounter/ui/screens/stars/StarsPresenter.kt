package com.example.gitstarscounter.ui.screens.stars

import android.util.Log
import com.example.gitstarscounter.data.repository.remote.entity.RemoteUser
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.data.providers.star.StarRepository
import com.example.gitstarscounter.data.repository.remote.entity.RemoteStar
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.jjoe64.graphview.series.DataPoint
import com.omegar.mvp.InjectViewState
import kotlinx.coroutines.launch

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter() : BasePresenter<StarsView>() {
    companion object {
        private const val YEAR_IS_NOW = 120 //java date need -1900
        const val TAG = "StarsPresenter"
        const val MESSAGE = "limited"
    }

    private val starsConvector = StarsConvector
    private var currYear: Int = YEAR_IS_NOW
    private var starsList = mutableListOf<Star>()
    private var error = false
    private var pageNumber = 1

    private val starRepository = StarRepository()

    private lateinit var userName: String
    private lateinit var repository: Repository

    fun setParams(userName: String, repositoryRemote: Repository) {
        this.userName = userName
        this.repository = repositoryRemote
    }

    fun responseToStartLoadStars() {
        starsList.clear()

        viewState.showSelectedYear(currYear.plus(1900), currYear < YEAR_IS_NOW)
        viewState.setWaiting(true)
        launch {
            val responseStarList =
                starRepository.getRepositoryStars(userName, repository)
            onStarsResponse(responseStarList)
        }
    }

    private fun loadGraph() {
        Log.d("CURR_YEAR", currYear.toString())
        starsConvector.setStarsMap(
            starsList,
            currYear
        )
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()

        viewState.setWaiting(false)
        viewState.setupStarsGrafic(pointsList, maxValueOfY.plus(1))
    }

    fun responseToChangeCurrentYear(more: Boolean) {
        if (more && (currYear + 1 <= YEAR_IS_NOW)) {
            currYear++
        } else if (!more) {
            currYear--
        }
        reloadStars()
    }

    private fun onStarsResponse(
        responseStarsList: List<Star>
    ) {
        responseStarsList.forEach {
            //     Log.d("StarsCallback", it.user.name + " $pageNumber")
            starsList.add(
                RemoteStar(
                    starredAt = it.starredAt,
                    user = RemoteUser(
                        id = it.user.id,
                        name = it.user.name,
                        avatarUrl = it.user.avatarUrl
                    )
                )
            )
        }
        loadGraph()
    }

    private fun reloadStars() {
        viewState.showSelectedYear(currYear.plus(1900), currYear < YEAR_IS_NOW)
        viewState.setWaiting(true)
        starsConvector.setStarsMap(
            starsList,
            currYear
        )
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()
        viewState.setupStarsGrafic(pointsList, maxValueOfY.plus(1))
        viewState.setWaiting(false)
    }

    fun responseToOpenUserStarred(x: Double) {
        val starsInMonthList = starsConvector.getStarListByMonth(x.toInt())
        viewState.openUsersStared(starsInMonthList)
    }
}
