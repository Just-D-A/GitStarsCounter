package com.example.gitstarscounter.service

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.gitstarscounter.data.local.providers.ServiceLocalProvider
import com.example.gitstarscounter.data.remote.RequestLimit
import com.example.gitstarscounter.data.remote.entity.RepositoryRemote
import com.example.gitstarscounter.data.remote.entity.StarRemote
import com.example.gitstarscounter.data.remote.entity.UserRemote
import com.example.gitstarscounter.data.remote.providers.ServiceRemoteProvider
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.entity.StarModel
import com.example.gitstarscounter.ui.screens.stars.StarsActivity

@RequiresApi(Build.VERSION_CODES.O)
class StarIntentService : IntentService("StarIntentService") {

    private val serviceRemoteProvider = ServiceRemoteProvider()
    private var serviceLocalProvider = ServiceLocalProvider()
    private var error = false

    /*
    Получить список всех репозиториев
    Получил все звезды с Api по интресуещим репозиториям  starListFromApi
    Отправить запрос к БД найти звезды которых не было(те которые вставили)
    Получить список НОВЫХ звезд которые были добавлены
    Отправить push уведомление
    */

    override fun onHandleIntent(intent: Intent?) {
        //super(onHandleIntent(intent))
        Log.d(TAG, "onHandleIntent")
        getNewStars()
        Log.d(TAG, "I_WILL_STOP")
        sendBroadcast(intent)
    }

    private fun getNewStars() {
        val repositoryModelList = serviceLocalProvider.getAllDatabaseRepositories()
        repositoryModelList.forEach {
            Log.d("REP_FROM_DB_IN_SERVICE", it.name)
            if (RequestLimit.limitResourceCount > 0) {
                startLoadStars(it)
                RequestLimit.subtractLimitResourceCount()
            } else {
                error = true
            }
        }
        Log.d(TAG, RequestLimit.limitResourceCount.toString())
    }

    private fun makeNotification(
        newStars: MutableList<StarModel>,
        repository: RepositoryModel
    ) {
        Log.d(LOG_TAG, "FIND ${newStars.size} NEW STARS IN REP: ${repository.name}")
        if (newStars.size > 0) {
            val channelId = createNotificationChannel(this, CHANNEL_ID, "Channel")
            val launcher = StarsActivity.createLauncher(repository.user.name, RepositoryRemote(repository.id, repository.name, repository.allStarsCount, UserRemote( repository.user.id, repository.user.name, repository.user.avatarUrl)), 50)
            val pendingIntent = launcher.getPendingIntent(this, 0, 0)

            val builder = NotificationCompat.Builder(
                applicationContext, channelId
            )
                .setContentTitle("Новые звезды")//Звезда
                .setContentText("У репозитория ${repository.name} появилось ${newStars.size} новых звезд")// N пользовательей поставили свои звезды репозиторию M
                .setSmallIcon(com.example.gitstarscounter.R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(
                applicationContext
            )
            notificationManager.notify(0, builder.build())
        }
    }

    private fun startLoadStars(repositoryRemote: RepositoryModel) {
        val pageNumber = 1
        //  val starList = mutableListOf<StarModel>()
        val remoteStarList = serviceRemoteProvider.loadStars(
            repositoryRemote.user.name,
            repositoryRemote,
            pageNumber
        )
        needMore(repositoryRemote, pageNumber, remoteStarList as MutableList<StarRemote>)
    }

    private fun needMore(
        repository: RepositoryModel,
        pageNumber: Int,
        remoteStarList: MutableList<StarRemote>
    ) {

        if (remoteStarList.size == 100) {
            val newPageNumber = pageNumber.plus(1)
            if (RequestLimit.limitResourceCount > 0) {
                remoteStarList.addAll(loadMoreStars(newPageNumber, repository))
                RequestLimit.subtractLimitResourceCount()
                needMore(repository, pageNumber, remoteStarList)
            } else {
                Log.d("Service", "NO REQUESTS")
            }
        } else if (!error) {
            Log.d(LOG_TAG, "ADD STARS TO REPOSITORY: ${repository.name}")
            Log.d(LOG_TAG, "ALL STARS SIZE ${remoteStarList.size}")
            val newStarList = serviceLocalProvider.findNewStars(remoteStarList, repository)
            makeNotification(newStarList, repository)
        }
    }

    private fun loadMoreStars(
        pageNumber: Int,
        repositoryRemote: RepositoryModel
    ): List<StarRemote> {
        return serviceRemoteProvider.loadStars(
            repositoryRemote.user.name,
            repositoryRemote,
            pageNumber
        ) as List<StarRemote>
    }

    private fun createNotificationChannel(
        context: Context,
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

    companion object {
        const val TAG = "StarIntentService"
        const val LOG_TAG = "StarIntentService"
        const val CHANNEL_ID = "com.example.gitstarscounter.service"
    }
}