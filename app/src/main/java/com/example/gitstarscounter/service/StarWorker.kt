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
import com.omega_r.base.errors.AppException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class StarWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    companion object {
        private const val TAG = "StarWorker"
        private const val CHANNEL_ID = "com.example.gitstarscounter.service"
        private const val CHANNEL_NAME = "Channel"
        private const val NOTIFICATION_TITLE = "Новые звезды"
        private const val FIRST_PART_OF_NOTIFICATION_MESSAGE = "Find "
        private const val SECOND_PART_OF_NOTIFICATION_MESSAGE = " NEW stars in repository: "
    }

    @Inject
    lateinit var workerRepository: WorkerRepository

    private var error = false

    init {
        GitStarsApplication.instance.gitStarsCounterComponent.inject(this)
    }

    override fun doWork(): Result {
        Log.d(TAG, "start")

        try {
            GlobalScope.launch {
                getNewStars()
            }
        } catch (e: Exception) {
            return Result.success()
        } catch (e: AppException.NoData) {
            return Result.success()
        }

        return Result.success()
    }

    private suspend fun getNewStars() {
        Log.d(TAG, "TRY GET NEW STARS")
        val repositoryModelList = workerRepository.getAllDatabaseRepositories()
        repositoryModelList.forEach {
            Log.d(TAG, it.name)
            RequestLimit.writeLog()
            startLoadStars(it)

        }

    }

    private fun makeNotification(
        newStars: List<Star>,
        repository: Repository
    ) {
        if (newStars.isNotEmpty()) {
            val channelId = createNotificationChannel()
            val launcher = StarsActivity.createLauncher(repository.user.name, repository)

            val pendingIntent = launcher.getPendingIntent(context, 0, 0)
            val str: Int = R.string.title_notification_new_stars

            val builder = NotificationCompat.Builder(
                applicationContext, channelId
            )
                .setContentTitle(NOTIFICATION_TITLE)//Star
                .setContentText(FIRST_PART_OF_NOTIFICATION_MESSAGE + newStars.size.toString() + SECOND_PART_OF_NOTIFICATION_MESSAGE + repository.name)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(
                applicationContext
            )
            notificationManager.notify(0, builder.build())
        }
    }

    private suspend fun startLoadStars(repository: Repository) {
        Log.d(TAG, "START LOAD STARS: ${repository.name}")
        val remoteStarList = workerRepository.loadStars(repository.user.name, repository)
        val newStarList = workerRepository.findNewStars(remoteStarList, repository)

        Log.d(TAG, "ADD STARS TO REPOSITORY: ${repository.name}")
        Log.d(TAG, "ALL STARS SIZE ${remoteStarList.size}")

        makeNotification(newStarList, repository)
    }

    private fun createNotificationChannel(): String {
        val chan =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return CHANNEL_ID
    }
}
