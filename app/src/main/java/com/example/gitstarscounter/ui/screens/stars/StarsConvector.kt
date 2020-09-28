@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.ui.screens.stars

import android.util.Log
import com.example.gitstarscounter.entity.Star
import com.jjoe64.graphview.series.DataPoint

@Suppress("DEPRECATION")
object StarsConvector {
    private const val MONTH_IN_YEAR: Int = 12
    private var starsInMonthMap: MutableMap<Int, MutableList<Star>> = mutableMapOf()

    fun toDataPoint(): ArrayList<DataPoint> {
        val pointsList: ArrayList<DataPoint> = ArrayList()
        starsInMonthMap.forEach {
            pointsList.add(DataPoint(it.key.toDouble(), it.value.size.toDouble()))
        }
        return pointsList
    }

    fun setStarsMap(starsList: List<Star>, currYear: Int) {
        initMap()
        starsList.forEach {
            val date = it.starredAt
            val year = date.year + 1900
            if (currYear == year) {
                val starList: MutableList<Star>? = starsInMonthMap[date.month + 1]
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
        starsInMonthMap.forEach {
            val starsInMonthList = it.value
            val starsCount = starsInMonthList.size.toDouble()
            if (result < starsCount) {
                result = starsCount
            }
        }

        return result
    }
}
