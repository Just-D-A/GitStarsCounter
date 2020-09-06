package com.example.gitstarscounter.stars

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.StarModel
import com.jjoe64.graphview.series.DataPoint

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
@InjectViewState
class StarsPresenter() : MvpPresenter<StarsView>(), StarsCallback {
    private val starsConvector = StarsConvector
    private val starsProvider = StarsProvider()
    private var currYear: Int = YEAR_IS_NOW
    private var starsList: MutableList<StarModel> = mutableListOf()
    private var error = false
    private var pageNumber = 1

    private  lateinit var userName: String
    private  lateinit var repositoryModel: RepositoryModel
    private  lateinit var starsEntityProvider: StarsEntityProvider


    fun setParams(userName: String, repositoryModel: RepositoryModel) {
        this.userName = userName
        this.repositoryModel = repositoryModel
        starsEntityProvider = StarsEntityProvider(this, repositoryModel)
    }

    fun startLoadStars() {
        viewState.showSelectedYear(currYear.plus(1900), currYear < YEAR_IS_NOW)
        viewState.startLoading()

        starsProvider.loadStars(userName, repositoryModel, pageNumber, this)
    }

    fun loadMoreStars(pageNumber: Int) {
        starsProvider.loadStars(userName, repositoryModel, pageNumber, this)
    }

    fun loadGrafic() {
        Log.d("CURR_YEAR", currYear.toString())
        starsConvector.setStarsMap(starsList, currYear)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()

        viewState.endLoading()
        viewState.setupStarsGrafic(pointsList, maxValueOfY.plus(1))
    }


    fun changeCurrentYear(more: Boolean) {
        if (more && (currYear + 1 <= YEAR_IS_NOW)) {
            currYear++
        } else if (!more) {
            currYear--
        }
        reloadStars()
    }

    override fun onStarsResponse(responseStarsList: List<StarModel>, noInternetIsVisible: Boolean) {
        viewState.changeVisibilityOfNoInternetView(noInternetIsVisible)
        responseStarsList.forEach {
            Log.d("StarsCallback", it.user.login)
            starsList.add(it)
        }
        needMore(responseStarsList)
    }

    override fun onError(textResource: Int) {
        viewState.endLoading()
        error = true
        starsEntityProvider.getRepositoryStars()
    }

    private fun reloadStars() {
        viewState.showSelectedYear(currYear.plus(1900), currYear < YEAR_IS_NOW)
        viewState.startLoading()
        starsConvector.setStarsMap(starsList, currYear)
        val pointsList: ArrayList<DataPoint> = starsConvector.toDataPoint()
        val maxValueOfY = starsConvector.getMaxCountValue()
        viewState.endLoading()
        viewState.setupStarsGrafic(pointsList, maxValueOfY.plus(1))
    }

    fun openUserStarred(x: Double) {
        val starsInMonthList = starsConvector.getStarListByMonth(x.toInt())
        viewState.openUsersStared(starsInMonthList)
    }

    private fun needMore(responseStarsList: List<StarModel>) {
       /* var lastStarYear = 0
        if (starsList.size != 0) {
            lastStarYear = starsList[starsList.size - 1].starredAt.year
        }
        val currStarsCount = starsList.size
        val allStarsCount = repositoryModel.allStarsCount*/
        if (responseStarsList.size == SearchStars.MAX_ELEMENTS_FROM_API) {
            pageNumber++
            loadMoreStars(pageNumber)
        } else {
            starsEntityProvider.insertToDatabase(starsList)
            starsEntityProvider.checkUnstars(starsList, repositoryModel)
            loadGrafic()
        }
    }

    companion object {
        private const val YEAR_IS_NOW = 120 //java date need -1900
    }
}
