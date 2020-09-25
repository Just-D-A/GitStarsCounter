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
import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.repository.local.providers.LocalStarWorkerProvider
import com.example.gitstarscounter.data.repository.remote.RequestLimit
import com.example.gitstarscounter.data.repository.remote.entity.RemoteRepository
import com.example.gitstarscounter.data.repository.remote.entity.RemoteUser
import com.example.gitstarscounter.data.repository.remote.providers.RemoteStarWorkerProvider
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.stars.StarsActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class StarWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    companion object {
        private const val TAG = "StarWorker"
        private const val CHANNEL_ID = "com.example.gitstarscounter.service"
        private const val CHANNEL_NAME = "Channel"
        private const val MAX_ANSWERS_COUNT_OF_REQUEST = 100
    }

    private val serviceRemoteProvider = RemoteStarWorkerProvider()
    private var serviceLocalProvider = LocalStarWorkerProvider()
    private var error = false

    override fun doWork(): Result {
        Log.d(TAG, "start")
        getNewStars()

        return Result.success()
    }

    private fun getNewStars() {
        GlobalScope.launch {
            val repositoryModelList = serviceLocalProvider.getAllDatabaseRepositories()
            repositoryModelList.forEach {
                Log.d(TAG, it.name)
                if (RequestLimit.hasRequest()) {
                    try{
                        startLoadStars(it)
                    } catch (e: Exception) {
                        error = true
                    }
                } else {
                    error = true
                }
            }
        }
        RequestLimit.writeLog()
    }

    private fun makeNotification(
        newStars: MutableList<com.example.gitstarscounter.entity.Star>,
        repository: Repository
    ) {
        Log.d(TAG, "FIND ${newStars.size} NEW STARS IN REP: ${repository.name}") // NO MAGIC
        if (newStars.size > 0) {
            val channelId = createNotificationChannel(CHANNEL_ID, CHANNEL_NAME)
            val launcher = StarsActivity.createLauncher(
                repository.user.name, RemoteRepository(
                    repository.id, repository.name, repository.allStarsCount, RemoteUser(
                        repository.user.id,
                        repository.user.name,
                        repository.user.avatarUrl
                    )
                )
            )

            val pendingIntent = launcher.getPendingIntent(context, 0, 0) //?? can I use 0 without pr

            val builder = NotificationCompat.Builder(
                applicationContext, channelId
            )
                .setContentTitle(R.string.title_notification_new_stars.toString())//Star
                .setContentText("У репозитория ${repository.name} появилось ${newStars.size} новых звезд") // MAGIC ENDS HERE
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(
                applicationContext
            )
            notificationManager.notify(0, builder.build())
        }
    }

    private suspend fun startLoadStars(repositoryRemote: Repository) {
        Log.d(TAG, repositoryRemote.name + "start load")
        val pageNumber = 1
        val remoteStarList = serviceRemoteProvider.loadStars(
            repositoryRemote.user.name,
            repositoryRemote,
            pageNumber
        )
        needMore(
            repositoryRemote,
            pageNumber,
            remoteStarList as MutableList<com.example.gitstarscounter.data.repository.remote.entity.RemoteStar>
        )
    }

    private fun needMore(
        repository: Repository,
        pageNumber: Int,
        listRemoteStar: MutableList<com.example.gitstarscounter.data.repository.remote.entity.RemoteStar>
    ) {
        GlobalScope.launch {
            if (listRemoteStar.size == MAX_ANSWERS_COUNT_OF_REQUEST) {
                val newPageNumber = pageNumber.plus(1)
                if (RequestLimit.hasRequest()) {
                    listRemoteStar.addAll(loadMoreStars(newPageNumber, repository))
                    needMore(repository, pageNumber, listRemoteStar)
                } else {
                    Log.d(TAG, "NO REQUESTS")
                }
            } else if (!error) {
                Log.d(TAG, "ADD STARS TO REPOSITORY: ${repository.name}")
                Log.d(TAG, "ALL STARS SIZE ${listRemoteStar.size}")

                val newStarList = serviceLocalProvider.findNewStars(listRemoteStar, repository)
                makeNotification(newStarList, repository)
            }
        }
    }

    private suspend fun loadMoreStars(
        pageNumber: Int,
        repositoryRemote: Repository
    ): List<com.example.gitstarscounter.data.repository.remote.entity.RemoteStar> {
        return serviceRemoteProvider.loadStars(
            repositoryRemote.user.name,
            repositoryRemote,
            pageNumber
        )!!
    }

    private fun createNotificationChannel(
        channelId: String,
        channelName: String
    ): String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}
