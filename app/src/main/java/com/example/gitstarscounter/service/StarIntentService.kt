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

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "com.example.gitstarscounter.service.action.FOO"
private const val ACTION_BAZ = "com.example.gitstarscounter.service.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.example.gitstarscounter.service.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.example.gitstarscounter.service.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class StarIntentService : IntentService("StarIntentService"), ServiceCallback {
    val LOG_TAG = "StarIntentService"

    private val YEAR_IS_NOW = 120 //java date need -1900
    val CHANNEL_ID = " com.example.gitstarscounter.service"
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
    private fun handleActionFoo(param1: String, param2: String) {
        TODO("Handle action Foo")
        Log.d(TAG, "handleActionFoo")
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionBaz(param1: String, param2: String) {
        TODO("Handle action Baz")
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
        // TODO: Customize helper method
        @JvmStatic
        fun startActionFoo(context: Context) {
            val intent = Intent(context, StarIntentService::class.java).apply {
                action = ACTION_FOO
            }
            Log.d(TAG, "startActionFoo")
            context.startService(intent)
        }


        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, StarIntentService::class.java).apply {
                action = ACTION_BAZ
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            Log.d(TAG, "startActionBaz")
            context.startService(intent)
        }
    }

    fun getNewStars() {
        ServiceEntity.getAllDatabaseRepositories(this)
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


}
