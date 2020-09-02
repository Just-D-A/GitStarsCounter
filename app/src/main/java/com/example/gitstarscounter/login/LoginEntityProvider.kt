@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.login


import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.gitstarscounter.entity.GitStarsDatabase
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class LoginEntityProvider(val loginCallback: LoginCallback) {
    val database = GitStarsDatabase.getDatabase()
    val userDao = database.userDao()
    val repositoryDao = database.repositoryDao()
    lateinit var userEntity: com.example.gitstarscounter.entity.user.User
    lateinit var repositoryEntity: com.example.gitstarscounter.entity.repository.Repository
    val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
    var repositoryTypeList: MutableList<Repository> = mutableListOf()


    fun checkDatabase() {
        val TAG = "DATA_BASE"

        if (userDao != null) {
            Log.d(TAG, "CREATED")
        } else {
            Log.d(TAG, "ERROR")
        }
    }

    fun insertUserAndRepository(user: User, repository: Repository) {

        // var check: com.example.gitstarscounter.entity.user.User? = null
        databaseWriteExecutor.execute {
            userDao?.insertAll(covertUserToEntity(user))
            repositoryDao?.insertAll(covertRepositoryToEntity(repository))
        }

    }

    fun getUsersRepositories(userName: String) {
        val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 0) {
                    loginCallback.onLoginResponse(repositoryTypeList, true)
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
                    repositoryTypeList.add(covertEntityToRepository(it, user))
                    Log.d("GET_FROM_DB", it.name)
                }

                handler.sendEmptyMessage(0)
            }
        }
    }


    private fun covertUserToEntity(user: User): com.example.gitstarscounter.entity.user.User {
        return com.example.gitstarscounter.entity.user.User(user.id, user.login, user.avatarUrl)
    }

    private fun covertRepositoryToEntity(repository: Repository): com.example.gitstarscounter.entity.repository.Repository {
        return com.example.gitstarscounter.entity.repository.Repository(
            repository.id,
            repository.name,
            repository.user.id
        )
    }

    private fun covertEntityToRepository(
        repository: com.example.gitstarscounter.entity.repository.Repository,
        user: com.example.gitstarscounter.entity.user.User
    ): Repository {
        return Repository(
            repository.id,
            repository.name.toString(),
            0,
            covertEntityToUser(user)
        )
    }


    private fun covertEntityToUser(user: com.example.gitstarscounter.entity.user.User): User {
        return User(user.id, user.name!!, user.avatarUrl)
    }

    companion object {
        private const val NUMBER_OF_THREADS = 4
    }
}