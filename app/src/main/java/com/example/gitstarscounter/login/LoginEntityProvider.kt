@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.login

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.gitstarscounter.entity.GitStarsDatabase
import com.example.gitstarscounter.entity.convectors.EntityConvector
import com.example.gitstarscounter.git_api.RepositoryModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LoginEntityProvider(val loginCallback: LoginCallback) {
    private val database = GitStarsDatabase.getDatabase()
    private val userDao = database.userDao()
    private val repositoryDao = database.repositoryDao()

    private var repositoryModelTypeList: MutableList<RepositoryModel> = mutableListOf()
    private val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    fun getUsersRepositories(userName: String) {
        val handler: Handler = @SuppressLint("HandlerLeak")
        object : Handler() {
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
