package com.example.gitstarscounter.stars

import com.example.gitstarscounter.retrofit2.Star
import com.jjoe64.graphview.series.DataPoint

@Suppress("DEPRECATION")
class StarsConvector(val starsList: List<Star?>?) {
    private var starsInMonthMap: MutableMap<Int, Int> = mutableMapOf()


    fun toDataPoint(): ArrayList<DataPoint> {
        val pointsList: ArrayList<DataPoint> = ArrayList()

        countStars()
        starsInMonthMap.forEach {
            pointsList.add(DataPoint(it.key.toDouble(), it.value.toDouble()))
        }
        pointsList.add(DataPoint(12.0, 20.0))
        return pointsList
    }

    private fun countStars() {
        initMap()
        starsList?.forEach {
            val date = it?.starred_at
            var starsCount: Int? = starsInMonthMap.get(date?.month)
            if (starsCount != null) {
                starsCount++
                starsInMonthMap.put(date?.month?.plus(1)!!, starsCount)
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

