package com.example.gitstarscounter.data.remote

import android.util.Log

object RequestLimit {
    var limitResourceCount = 0

    fun subtractLimitResourceCount() {
        if(limitResourceCount > 0) {
            limitResourceCount--
        } else {
            Log.d("ЭТОГО", "НЕ МОЖЕТ БЫТЬ!!!RequestLimit")
        }
    }
}