package com.example.gitstarscounter.service

import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.gitstarscounter.entity.GitStarsDatabase
import com.example.gitstarscounter.entity.convectors.EntityConvector
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.StarModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ServiceEntity {
    private val database = GitStarsDatabase.getDatabase()
    private val userDao = database.userDao()
    private val repositoryDao = database.repositoryDao()
    private val starDao = database.starDao()
    private val repositoryModelList = mutableListOf<RepositoryModel>()
    private const val NUMBER_OF_THREADS = 4
    private val databaseWriteExecutor: ExecutorService =
        Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    fun getAllDatabaseRepositories(serviceCallback: ServiceCallback) {
        val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 0) {
                    Log.d("SERVICE FB OBJECT2", repositoryModelList?.size.toString())
                    serviceCallback.onDatabaseRepositoryResponse(repositoryModelList)
                }
            }
        }
        databaseWriteExecutor.execute {

            val repositoryEntityList = repositoryDao?.getAll()
            Log.d("SERVICE FB OBJECT", repositoryEntityList?.size.toString())
            repositoryEntityList?.forEach {
                val user = userDao?.getUserById(it.userId)
                repositoryModelList.add(EntityConvector.covertEntityToRepository(it, user!!))
            }
            handler.sendEmptyMessage(0)
        }
    }

    fun findNewStars(serviceCallback: ServiceCallback, starListFromApi: List<StarModel>, repositoryModel: RepositoryModel) {
        val newStars = mutableListOf<StarModel>()
        val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 0) {
                    serviceCallback.onFindStarResponse(newStars, repositoryModel)
                }
            }
        }
        databaseWriteExecutor.execute {
            starListFromApi.forEach { starFromApi -> //стоит ли так делать или лучше it??
                val starFromDB = starDao?.findByRepositoryUserAndId(repositoryModel.id, starFromApi.user.id)
                if (starFromDB == null) {
                    newStars.add(starFromApi)
                    starDao?.insertAll(EntityConvector.convertStarToEntity(starFromApi, repositoryModel.id))
                }
            }
            handler.sendEmptyMessage(0)
        }

    }


}