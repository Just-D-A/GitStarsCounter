@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.data.local.providers

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.local.entity.RepositoryLocal
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.ui.screens.login.LoginCallback
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LoginLocalProvider(val loginCallback: LoginCallback) {
    private val database = GitStarsApplication.instance?.database!!
    private val userDao = database.userDao()
    private val repositoryDao = database.repositoryDao()

    private val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(
        NUMBER_OF_THREADS
    )
    private var repositoryRemoteTypeList: MutableList<RepositoryModel> = mutableListOf()

    fun getUsersRepositories(userName: String) {
        val handler: Handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 0) {
                    loginCallback.onLoginResponse(repositoryRemoteTypeList, true)
                } else if (msg.what == 1) {
                    loginCallback.onUnknownUser(R.string.unknown_database_user, true)
                }
            }
        }

        databaseWriteExecutor.execute {
            val user = userDao?.getUserByName(userName) //запрос к БД
            Log.d("DATA_BASE", "${user?.name} getted")

            if (user == null) {
                handler.sendEmptyMessage(1)
            }

            user?.let { user ->
                val repositoriesList = repositoryDao?.getRepositoriesByUserId(user.id)
                repositoriesList?.forEach {
                    repositoryRemoteTypeList.add(RepositoryLocal(it))
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
