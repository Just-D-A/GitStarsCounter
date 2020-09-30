package com.example.gitstarscounter.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.providers.worker.WorkerRepository
import com.example.gitstarscounter.data.repository.remote.RequestLimit
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.ui.screens.stars.StarsActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class LimitWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    companion object {
        private const val TAG = "LimitWorker"
    }

    @Inject
    lateinit var workerRepository: WorkerRepository

    private var error = false

    init {
        GitStarsApplication.instance.gitStarsCounterComponent.inject(this)
    }

    override fun doWork(): Result {
        Log.d(TAG, "start")

        GlobalScope.launch {
            updateRateLimit()
        }

        return Result.success()
    }

    private suspend fun updateRateLimit() {
        try {
            val limit = workerRepository.getLimitRemaining().resources.core.remaining
            RequestLimit.setLimitResourceCount(limit)
            RequestLimit.writeLog()
        } catch (e: Exception) {
            error = true
            Log.d(TAG, "NO_INTERNET")
        }
    }
}
