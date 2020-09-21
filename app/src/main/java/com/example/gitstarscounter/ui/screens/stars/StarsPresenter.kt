package com.example.gitstarscounter.ui.screens.stars

import android.util.Log
import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.repository.remote.RequestLimit
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
    private var starsList: MutableList<RemoteStar> =
        mutableListOf()
    private var error = false
    private var pageNumber = 1

    private val repositoryStarProvider = StarRepository()

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
            if (RequestLimit.hasRequest()) {
                val responseStarList =
                    repositoryStarProvider.getRepositoryStars(userName, repository, pageNumber)
                onStarsResponse(responseStarList!!, false)
            } else {
                showDatabaseMessage()
            }
        }
    }

    private fun loadMoreStars(pageNumber: Int) {
        launch {
            if (RequestLimit.hasRequest()) {
                val responseStarList =
                    repositoryStarProvider.getRepositoryStars(userName, repository, pageNumber)
                if (responseStarList != null) {
                    onStarsResponse(responseStarList, false)
                } else {
                    onError(R.string.no_internet_message)
                }
            } else {
                showDatabaseMessage()
            }
        }
    }

    private fun loadGraph() {
        Log.d("CURR_YEAR", currYear.toString())
        starsConvector.setStarsMap(
            starsList as List<RemoteStar>,
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

    fun onStarsResponse(
        responseStarsList: List<Star>,
        noInternetIsVisible: Boolean
    ) {
        viewState.changeVisibilityOfDataMessage(noInternetIsVisible)
        responseStarsList.forEach {
            Log.d("StarsCallback", it.user.name + " $pageNumber")
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
        needMore(responseStarsList)
    }

    private fun onError(textResource: Int) {
        viewState.setWaiting(false)
        showDatabaseMessage()
    }

    private fun reloadStars() {
        viewState.showSelectedYear(currYear.plus(1900), currYear < YEAR_IS_NOW)
        viewState.setWaiting(true)
        starsConvector.setStarsMap(
            starsList as List<RemoteStar>,
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

    private fun needMore(responseStarsList: List<Star>) {
        if ((responseStarsList.size == SearchStars.MAX_ELEMENTS_FROM_API) && !error) {
            pageNumber++
            loadMoreStars(pageNumber)
        } else {
            loadGraph()
        }
    }

    private fun showDatabaseMessage() {
        viewState.changeVisibilityOfDataMessage(true)
        Log.d(TAG, MESSAGE)
        getStarsFromDatabase()
        viewState.setWaiting(false)
    }

    private fun getStarsFromDatabase() {
        error = true
        launch {
            val responseStarsList =
                repositoryStarProvider.getRepositoryStars(userName, repository, 0)
            onStarsResponse(responseStarsList!!, true)
        }
    }
}
