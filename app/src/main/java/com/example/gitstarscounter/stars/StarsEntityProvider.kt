@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.stars


import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.gitstarscounter.entity.GitStarsDatabase
import com.example.gitstarscounter.entity.convectors.EntityConvector
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.StarModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class StarsEntityProvider(val starsCallback: StarsCallback, val repositoryModel: RepositoryModel) {
    val database = GitStarsDatabase.getDatabase()
    val userDao = database.userDao()
    val repositoryDao = database.repositoryDao()
    val starDao = database.starDao()
    val databaseWriteExecutor: ExecutorService =
        Executors.newFixedThreadPool(NUMBER_OF_THREADS)
    var starsTypeList: MutableList<StarModel> = mutableListOf()


    fun checkDatabase() {
        val TAG = "DATA_BASE"

        if (userDao != null) {
            Log.d(TAG, "CREATED")
        } else {
            Log.d(TAG, "ERROR")
        }
    }

    fun insertToDatabase(starModelList: List<StarModel>) {
        databaseWriteExecutor.execute {
            userDao?.insertAll(EntityConvector.covertUserToEntity(repositoryModel.user))
            repositoryDao?.insertAll(EntityConvector.covertRepositoryToEntity(repositoryModel))

            val repositoryLocal = repositoryDao?.getRepositoryById(repositoryModel.id)

            val list = repositoryDao?.getAll()
            list?.forEach { lol ->
                Log.d("REP_NAME", lol.name)
            }
            starModelList.forEach {
                val star = EntityConvector.convertStarToEntity(
                    it,
                    repositoryLocal?.id!!
                )


                    userDao?.insertAll(EntityConvector.covertUserToEntity(it.user))

                val user = userDao?.getUserById(star.userId)
                val starFromDB = starDao?.findByRepositoryUserAndId(star.repositoryId, star.userId)
                if(starFromDB == null) {
                    starDao?.insertAll(star)
                }
            }
            Log.d("DATA_BASE", "ALL ADDED")
        }

    }

    fun getRepositoryStars() {
        val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 0) {
                    starsCallback.onStarsResponse(starsTypeList, true)
                }
            }
        }
        databaseWriteExecutor.execute {
            val starsList = starDao?.findByRepositoryId(repositoryModel.id)
            starsList?.forEach {
                starsTypeList.add(EntityConvector.covertEntityToStar(it, userDao?.getUserById(it.userId)!!))
            }
            handler.sendEmptyMessage(0)
        }
    }


    companion object {
        private const val NUMBER_OF_THREADS = 4
        var sdf3: SimpleDateFormat =
            SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
    }
}
