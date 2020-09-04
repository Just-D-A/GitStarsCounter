@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.login


import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.gitstarscounter.entity.GitStarsDatabase
import com.example.gitstarscounter.entity.convectors.EntityConvector
import com.example.gitstarscounter.entity.user.User
import com.example.gitstarscounter.git_api.RepositoryModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class LoginEntityProvider(val loginCallback: LoginCallback) {
    val database = GitStarsDatabase.getDatabase()
    val userDao = database.userDao()
    val repositoryDao = database.repositoryDao()
    lateinit var userEntity: User
    lateinit var repositoryEntity: com.example.gitstarscounter.entity.repository.Repository
    val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
    var repositoryModelTypeList: MutableList<RepositoryModel> = mutableListOf()

    fun getUsersRepositories(userName: String) {
        val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 0) {
                    loginCallback.onLoginResponse(repositoryModelTypeList, true)
                }
            }
        }
        databaseWriteExecutor.execute {
            val user = userDao?.getUserByName(userName) //запрос к БД

            Log.d("DATA_BASE", "${user?.name} getted")

            user?.let { user ->
                val repositoriesList = repositoryDao?.getRepositoriesByUserId(user.id)

                if (repositoriesList?.isEmpty()!!) {
                    Log.d("REP", "EMPTY REP LIST")
                }
                repositoriesList?.forEach {
                    repositoryModelTypeList.add(EntityConvector.covertEntityToRepository(it, user))
                    Log.d("GET_FROM_DB", it.name)
                }
                handler.sendEmptyMessage(0)
            }
        }
    }

    companion object {
        private const val NUMBER_OF_THREADS = 4
    }

}