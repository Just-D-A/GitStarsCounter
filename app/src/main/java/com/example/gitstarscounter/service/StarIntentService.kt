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
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.StarModel
import com.example.gitstarscounter.stars.StarsActivity

class StarIntentService : IntentService("StarIntentService"), ServiceCallback {
    private val LOG_TAG = "StarIntentService"
    private val YEAR_IS_NOW = 120 //java date need -1900
    private val CHANNEL_ID = " com.example.gitstarscounter.service"
    private val serviceProvider = ServiceProvider()
    private var error = false

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent")
        getNewStars()
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo() {
        Log.d(TAG, "handleActionFoo")

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionBaz(param1: String, param2: String) {
        Log.d(TAG, "handleActionBaz")
    }

    companion object {
        val TAG = "StarIntentService"

        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionFoo(context: Context) {

            Log.d(TAG, "startActionFoo")
            // context.startService(intent)
        }


        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */

        @JvmStatic
        fun startActionBaz(context: Context, param1: String, param2: String) {

            Log.d(TAG, "startActionBaz")
            //  context.startService(intent)
        }
    }

    //Получить список всех репозиториев
    //Получил все звезды с Api по интресуещим репозиториям  starListFromApi ++++++++++++
    //Отправить запрос к БД найти звезды которых не было(те которые вставили)
    //Получить список НОВЫХ звезд которые были добавлены
    //Отправить push уведомление
    fun getNewStars() {
        ServiceEntity.getAllDatabaseRepositories(this)
    }

    override fun onDatabaseRepositoryResponse(repositoryModelList: List<RepositoryModel>) {
        //Получить список всех репозиториев
        repositoryModelList.forEach {
            Log.d("REP_FROM_DB_IN_SERVICE", it.name)
            startLoadStars(it)
        }


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
    override fun onDatabaseFindStarResponse(
        newStars: MutableList<StarModel>,
        repositoryModel: RepositoryModel
    ) {
        Log.d(LOG_TAG, "FIND ${newStars.size} NEW STARS IN REP: ${repositoryModel.name}")
        if (newStars.size > 0) {
            val channelId = createNotificationChannel(this, CHANNEL_ID, "Channel")
            /*val intent = StarsActivity.createIntent(this, repositoryModel.user.login, repositoryModel)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)*/
            val launcher = StarsActivity.createLauncher(repositoryModel.user.login, repositoryModel)
            val pendingIntent = launcher.getPendingIntent(this, 0, 0)
            val builder = NotificationCompat.Builder(
                applicationContext, channelId
            )
                .setContentTitle("Новые звезды")//Звезда
                .setContentText("У репозитория ${repositoryModel.name} появилось ${newStars.size} новых звезд")// N пользовательей поставили свои звезды репозиторию M
                .setSmallIcon(com.example.gitstarscounter.R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
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
}