@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.data.local.providers

import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.local.database.enitity.repository.TableRepository
import com.example.gitstarscounter.data.local.database.enitity.star.TableStar
import com.example.gitstarscounter.data.local.entity.UserLocal
import com.example.gitstarscounter.data.local.entity.StarLocal
import com.example.gitstarscounter.data.remote.entity.StarRemote
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.entity.StarModel
import com.example.gitstarscounter.ui.screens.stars.StarsCallback
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class StarsLocalProvider(val starsCallback: StarsCallback, val repositoryRemote: RepositoryModel) {
    private val database = GitStarsApplication.instance?.database!!
    private val userDao = database.userDao()
    private val repositoryDao = database.repositoryDao()
    private val starDao = database.starDao()

    private val databaseWriteExecutor: ExecutorService =
        Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    fun insertToDatabase(starRemoteList: List<StarRemote>) {
         databaseWriteExecutor.execute {
            userDao?.insertAll(UserLocal( repositoryRemote.user))
            repositoryDao?.insertAll(TableRepository(repositoryRemote))

        //    val repositoryLocal = repositoryDao?.getRepositoryById(repositoryRemote.id)
            val list = repositoryDao?.getAll()
            list?.forEach { lol ->
                Log.d("REP_NAME", lol.name)
            }
            starRemoteList.forEach {
                val star = StarLocal(
                    it,
                    repositoryRemote
                )

                userDao?.insertAll(UserLocal(it.user))

                val starFromDB = starDao?.findByRepositoryUserAndId(star.repository.id, star.user.id)
                if (starFromDB == null) {
                    starDao?.insertAll(TableStar(star))
                    Log.d("StarProvider", "add new star to DB")
                }
            }
            Log.d("DATA_BASE", "ALL ADDED")
        }
    }

    fun checkUnstars(starsListFromApi: List<StarRemote>, repositoryRemote: RepositoryModel) {
        databaseWriteExecutor.execute {
            //взять все звезды из бд по id rep
            val starsListFromDB = starDao?.findByRepositoryId(repositoryRemote.id)
            //создать map по ключу star.user из star-api
            val starsMap = mutableMapOf<Long, TableStar>()
            starsListFromApi.forEach {
                val star = TableStar(StarLocal(it, repositoryRemote))
                starsMap[star.userId] = star
            }
            //для каждой звезды с БД найти звезду с api по id_user если не найдено, значит удалить\
            var i = 0
            starsListFromDB?.forEach {
                val starFromApi = starsMap[it.userId]
                if (starFromApi == null) {
                    starDao?.delete(it)
                    i++
                }
            }
            Log.d("Database", "DELETED $i STARS")
        }
    }

    fun getRepositoryStars() {
        var starsTypeList: MutableList<StarModel> = mutableListOf()
        val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 0) {
                    starsCallback.onStarsResponse(starsTypeList , true)
                }
            }
        }
        databaseWriteExecutor.execute {
            val starsList = starDao?.findByRepositoryId(repositoryRemote.id)
            starsList?.forEach {
                starsTypeList.add(
                    StarLocal(it)
                )
            }
            handler.sendEmptyMessage(0)
        }
    }

    companion object {
        private const val NUMBER_OF_THREADS = 4
    }
}
