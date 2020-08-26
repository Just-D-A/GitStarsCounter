@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.stars

import android.util.Log
import com.example.gitstarscounter.git_api.Star
import com.jjoe64.graphview.series.DataPoint

@Suppress("DEPRECATION")
object StarsConvector {
    private var starsInMonthMap: MutableMap<Int, MutableList<Star>> = mutableMapOf()
    const val MONTH_IN_YEAR: Int = 12

    fun toDataPoint(): ArrayList<DataPoint> {
        val pointsList: ArrayList<DataPoint> = ArrayList()
        starsInMonthMap.forEach {
            pointsList.add(DataPoint(it.key.toDouble(), it.value.size.toDouble()))
        }
        return pointsList
    }

    fun setStarsMap(starsList: List<Star?>?, currYear: Int) {
        initMap()
        starsList?.forEach {
            val date = it!!.starred_at
            Log.d("YEAR", date.year.toString())
            if (currYear == date.year) {
                val starList: MutableList<Star>? = starsInMonthMap[date.month.plus(1)]
                starList?.add(it)
                starsInMonthMap[date.month.plus(1)] = starList!!
            }
        }
    }

    private fun initMap() {
        for (i in 1..MONTH_IN_YEAR) {
            starsInMonthMap[i] = mutableListOf()
        }
    }

    fun getStarListByMonth(monthNumber: Int): MutableList<Star> {
        return starsInMonthMap.get(monthNumber)!!
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

