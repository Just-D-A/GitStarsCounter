package com.example.gitstarscounter.service

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.StarModel


@Suppress("DEPRECATION")
class StarService : Service(), ServiceCallback {
    val LOG_TAG = "SERVICE"

    //   var starsList = mutableListOf<StarModel>()
    private val serviceProvider = ServiceProvider()
    private var error = false
    // var starListFromApi = mutableListOf<StarModel>()

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "onStartCommand")
        getNewStars()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "onDestroy")
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(LOG_TAG, "onBind")
        return null
    }

    fun getNewStars() {
        ServiceEntity.getAllDatabaseRepositories(this)
    }

    override fun onDatabaseStarResponse(starModelList: List<StarModel>) {
        TODO("Not yet implemented")
    }

    override fun onDatabaseRepositoryResponse(repositoryModelList: List<RepositoryModel>) {
        //Получить список всех репозиториев
        repositoryModelList.forEach {
            Log.d("REP_FROM_DB_IN_SERVICE", it.name)
            startLoadStars(it)
        }
        //Получил все звезды с Api по интресуещим репозиториям  starListFromApi ++++++++++++
        //Отправить запрос к БД найти звезды которых не было(те которые вставили)
        //Получить список НОВЫХ звезд которые были добавлены
        //Отправить push уведомление

    }


    override fun onStarsResponse(
        responseStarsList: MutableList<StarModel>,
        repositoryModel: RepositoryModel,
        pageNumber: Int
    ) {

        needMore(repositoryModel, pageNumber, responseStarsList)
    }

    override fun onError(textResource: Int) {
        error = true
        Log.d(LOG_TAG, "INTERNET_ERROR")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFindStarResponse(
        newStars: MutableList<StarModel>,
        repositoryModel: RepositoryModel
    ) {
        Log.d(LOG_TAG, "FIND ${newStars.size} NEW STARS IN REP: ${repositoryModel.name}")
        if (newStars.size > 0) {
            val channelId = createNotificationChannel(this, CHANNEL_ID, "Channel")
            val builder = NotificationCompat.Builder(
                applicationContext, channelId
            )
                .setContentTitle("Новые звезды")//Звезда тебе, парень
                .setContentText("У репозитория ${repositoryModel.name} появилось ${newStars.size} новых звезд")// N пользовательей поставили свои звезды репозиторию M
                .setSmallIcon(com.example.gitstarscounter.R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(
                applicationContext
            )
            notificationManager.notify(0, builder.build())
        }


    }

    private fun startLoadStars(repositoryModel: RepositoryModel) {
        val pageNumber = 1
        val starList = mutableListOf<StarModel>()
        serviceProvider.loadStars(
            repositoryModel.user.login,
            repositoryModel,
            pageNumber,
            this,
            starList
        )
    }

    private fun needMore(
        repositoryModel: RepositoryModel,
        pageNumber: Int,
        starsList: MutableList<StarModel>
    ) {
        var lastStarYear = 0
        if (starsList.size != 0) {
            lastStarYear = starsList[starsList.size - 1].starredAt.year
        }
        val currStarsCount = starsList.size
        val allStarsCount = repositoryModel.allStarsCount
        if ((lastStarYear <= YEAR_IS_NOW) && (currStarsCount < allStarsCount) && (!error)) {
            val newPageNumber = pageNumber.plus(1)
            loadMoreStars(newPageNumber, repositoryModel, starsList)
        } else if (!error) {
            Log.d(LOG_TAG, "ADD STARS TO REPOSITORY: ${repositoryModel.name}")
            Log.d(LOG_TAG, "ALL STARS SIZE ${starsList.size}")
            ServiceEntity.findNewStars(this, starsList, repositoryModel)
        }
    }

    fun loadMoreStars(
        pageNumber: Int,
        repositoryModel: RepositoryModel,
        starsList: MutableList<StarModel>
    ) {
        serviceProvider.loadStars(
            repositoryModel.user.login,
            repositoryModel,
            pageNumber,
            this,
            starsList
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(
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
        private const val YEAR_IS_NOW = 120 //java date need -1900
        const val CHANNEL_ID = " com.example.gitstarscounter.service"
    }
}