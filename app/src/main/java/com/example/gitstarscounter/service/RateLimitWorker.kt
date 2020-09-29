package com.example.gitstarscounter.service

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.providers.login.LoginRepository
import com.example.gitstarscounter.data.providers.worker.WorkerRepository
import com.example.gitstarscounter.data.repository.remote.RequestLimit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class RateLimitWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    companion object {
        private const val TAG = "RateLimitWorker"
    }

    @Inject
    lateinit var workerRepository: WorkerRepository

    init {
        GitStarsApplication.instance.gitStarsCounterComponent.inject(this)
    }

    override fun doWork(): Result {
        Log.d(TAG, "start")
        updateRateLimit()

        return Result.success()
    }

    private fun updateRateLimit() {
        GlobalScope.launch {
            try {
                val limit = workerRepository.getLimitRemaining().resources.core.remaining
                RequestLimit.setLimitResourceCount(limit)
                RequestLimit.writeLog()
            } catch (e: Exception) {
                Log.d(TAG, "NO_INTERNET")
            }
        }
    }
}
