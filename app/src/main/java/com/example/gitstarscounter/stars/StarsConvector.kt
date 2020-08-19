package com.example.gitstarscounter.stars

import com.jjoe64.graphview.series.DataPoint
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class StarsConvector(val starsList: ArrayList<StarModel>) {
    private var starsInMonthMap: MutableMap<Int, Int> = mutableMapOf()


    fun toDataPoint(): ArrayList<DataPoint> {
        val pointsList: ArrayList<DataPoint> = ArrayList()

        countStars()
        starsInMonthMap.forEach {
            pointsList.add(DataPoint(it.key.toDouble(), it.value.toDouble()))
        }
        pointsList.add(DataPoint(12.0, 12.0))
        return pointsList
    }

    private fun countStars() {
        initMap()
        starsList.forEach {
            val date: Date = it.date
            var starsCount: Int? = starsInMonthMap.get(date.month)
            if (starsCount != null) {
                starsCount++
                starsInMonthMap.put(date.month, starsCount)
            }
        }
    }

    private fun initMap() {
        for (i in 1..MONTH_IN_YEAR) {
            starsInMonthMap[i] = 0
        }
    }

    companion object {
        const val MONTH_IN_YEAR: Int = 12
    }
}

