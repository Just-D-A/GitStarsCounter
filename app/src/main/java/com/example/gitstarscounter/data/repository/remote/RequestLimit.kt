package com.example.gitstarscounter.data.repository.remote

import android.util.Log

object RequestLimit {
    private const val TAG = "RequestLimit"
    private var limitResourceCount = 0

    fun setLimitResourceCount(newValue: Int) {
        limitResourceCount = newValue
    }

    fun hasRequest(): Boolean {
        return if (limitResourceCount > 0) {
            limitResourceCount--
            true
        } else {
            false
        }
    }

    fun writeLog() {
        Log.d(TAG, limitResourceCount.toString())
    }
}
