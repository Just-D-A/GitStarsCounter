package com.example.gitstarscounter.ui.screens.stars

import android.util.Log
import com.example.gitstarscounter.data.local.providers.StarsLocalProvider
import com.example.gitstarscounter.data.remote.RequestLimit
import com.example.gitstarscounter.data.remote.entity.StarRemote
import com.example.gitstarscounter.data.remote.entity.UserRemote
import com.example.gitstarscounter.data.remote.providers.StarsRemoteProvider
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.entity.StarModel
import com.example.gitstarscounter.ui.screens.base.BasePresenter
import com.jjoe64.graphview.series.DataPoint
import com.omegar.mvp.InjectViewState

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter() : BasePresenter<StarsView>(), StarsCallback {
    private val starsConvector = StarsConvector
    private val starsProvider = StarsRemoteProvider()
    private var currYear: Int = YEAR_IS_NOW
    private var starsList: MutableList<StarRemote> = mutableListOf()
    private var error = false
    private var pageNumber = 1

    private lateinit var userName: String
    private lateinit var repositoryModel: RepositoryModel
    private lateinit var starsEntityProvider: StarsLocalProvider


    fun setParams(userName: String, repositoryRemote: RepositoryModel) {
        this.userName = userName
        this.repositoryModel = repositoryRemote
        starsEntityProvider = StarsLocalProvider(this, repositoryRemote)
    }

    fun responseToStartLoadStars() {
        starsList.clear()

        viewState.showSelectedYear(currYear.plus(1900), currYear < YEAR_IS_NOW)
        viewState.setWaiting(true)

        if (RequestLimit.limitResourceCount > 0) {
            starsProvider.loadStars(userName, repositoryModel, pageNumber, this)
            RequestLimit.subtractLimitResourceCount()
        } else {
            showDatabaseMessage()
        }
    }

    private fun loadMoreStars(pageNumber: Int) {
        if (RequestLimit.limitResourceCount > 0) {
            starsProvider.loadStars(userName, repositoryModel, pageNumber, this)
            RequestLimit.subtractLimitResourceCount()
        } else {
            showDatabaseMessage()
        }
    }

    private fun loadGraph() {
        Log.d("CURR_YEAR", currYear.toString())
        starsConvector.setStarsMap(starsList as List<StarRemote>, currYear)
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

    override fun onStarsResponse(responseStarsList: List<StarModel>, noInternetIsVisible: Boolean) {
        viewState.changeVisibilityOfDataMessage(noInternetIsVisible)
        responseStarsList.forEach {
            Log.d("StarsCallback", it.user.name + " $pageNumber")
            starsList.add(
                StarRemote(
                    starredAt = it.starredAt,
                    user = UserRemote(
                        id = it.user.id,
                        name = it.user.name,
                        avatarUrl = it.user.avatarUrl
                    )
                )
            )
        }
        needMore(responseStarsList)
    }

    override fun onError(textResource: Int) {
        viewState.setWaiting(false)
        showDatabaseMessage()
    }

    private fun reloadStars() {
        viewState.showSelectedYear(currYear.plus(1900), currYear < YEAR_IS_NOW)
        viewState.setWaiting(true)
        starsConvector.setStarsMap(starsList as List<StarRemote>, currYear)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()
        viewState.setupStarsGrafic(pointsList, maxValueOfY.plus(1))
        viewState.setWaiting(false)
    }

    fun responseToOpenUserStarred(x: Double) {
        val starsInMonthList = starsConvector.getStarListByMonth(x.toInt())
        viewState.openUsersStared(starsInMonthList)
    }

    private fun needMore(responseStarsList: List<StarModel>) {
        if ((responseStarsList.size == SearchStars.MAX_ELEMENTS_FROM_API) && !error) {
            pageNumber++
            loadMoreStars(pageNumber)
        } else {
            if (!error) {
                starsEntityProvider.insertToDatabase(starsList as List<StarRemote>)
                starsEntityProvider.checkUnstars(starsList as List<StarRemote>, repositoryModel)
            }
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
        starsEntityProvider.getRepositoryStars()
    }

    companion object {
        private const val YEAR_IS_NOW = 120 //java date need -1900
        const val TAG = "StarsPresenter"
        const val MESSAGE = "limited"
    }
}
