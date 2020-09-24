package com.example.gitstarscounter.service

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.gitstarscounter.data.providers.login.LoginRepository
import com.example.gitstarscounter.data.repository.remote.RequestLimit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class RateLimitWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    companion object {
        private const val TAG = "RateLimitWorker"

    }

    override fun doWork(): Result {
        Log.d(TAG, "start")
        updateRateLimit()

        return Result.success()
    }

    private fun updateRateLimit() {
        GlobalScope.launch {
            RequestLimit.setLimitResourceCount(LoginRepository().getLimitRemaining().resources.core.remaining)
        }
    }
}
