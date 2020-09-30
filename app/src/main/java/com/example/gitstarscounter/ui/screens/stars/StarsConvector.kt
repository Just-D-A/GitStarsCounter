@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.ui.screens.stars

import com.example.gitstarscounter.entity.Star
import com.jjoe64.graphview.series.DataPoint

@Suppress("DEPRECATION")
class StarsConvector(private val starsList: List<Star>, private val currYear: Int) {
    companion object {
        private const val MONTH_IN_YEAR: Int = 12
        private const val JAVA_YEAR_DELTA = 1900
    }

    private var starsInMonthMap: MutableMap<Int, MutableList<Star>> = mutableMapOf()

    init {
        initMap()
        setStarsMap()
    }

    fun toDataPoint(): ArrayList<DataPoint> {
        val pointsList: ArrayList<DataPoint> = ArrayList()
        starsInMonthMap.map {
            pointsList.add(DataPoint(it.key.toDouble(), it.value.size.toDouble()))
        }
        return pointsList
    }

    private fun setStarsMap() {
        starsList.map {
            val date = it.starredAt
            val year = date.year + JAVA_YEAR_DELTA
            if (currYear == year) {
                val starList = starsInMonthMap[date.month + 1]
                starList?.add(it)
                starsInMonthMap[date.month + 1] = starList!!
            }
        }
    }

    private fun initMap() {
        for (i in 1..MONTH_IN_YEAR) {
            starsInMonthMap[i] = mutableListOf()
        }
    }

    fun getStarListByMonth(monthNumber: Int): MutableList<Star> {
        return starsInMonthMap[monthNumber]!!
    }

    fun getMaxCountValue(): Double {
        var result: Double = starsInMonthMap[1]!!.size.toDouble()
        starsInMonthMap.map {
            val starsInMonthList = it.value
            val starsCount = starsInMonthList.size.toDouble()
            if (result < starsCount) {
                result = starsCount
            }
        }

        return result
    }
}
