@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.stars


import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.gitstarscounter.entity.GitStarsDatabase
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.git_api.Star
import com.example.gitstarscounter.git_api.User
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class StarsEntityProvider(val starsCallback: StarsCallback, val repository: Repository) {
    val database = GitStarsDatabase.getDatabase()
    val userDao = database.userDao()
    val repositoryDao = database.repositoryDao()
    val starDao = database.starDao()
    val databaseWriteExecutor: ExecutorService =
        Executors.newFixedThreadPool(NUMBER_OF_THREADS)
    var starsTypeList: MutableList<Star> = mutableListOf()


    fun checkDatabase() {
        val TAG = "DATA_BASE"

        if (userDao != null) {
            Log.d(TAG, "CREATED")
        } else {
            Log.d(TAG, "ERROR")
        }
    }

    fun insertToDatabase(starList: List<Star>) {
        databaseWriteExecutor.execute {
            userDao?.insertAll(covertUserToEntity(repository.user))
            repositoryDao?.insertAll(covertRepositoryToEntity())

            val repositoryLocal = repositoryDao?.getRepositoryById(repository.id)

            val list = repositoryDao?.getAll()
            list?.forEach { lol ->
                Log.d("REP_NAME", lol.name)
            }
            starList.forEach {
                val star = convertStarToEntity(
                    it,
                    repositoryLocal?.id!!
                )


                    userDao?.insertAll(covertUserToEntity(it.user))

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
            val starsList = starDao?.findByRepositoryId(repository.id)
            starsList?.forEach {
                starsTypeList.add(covertEntityToStar(it, userDao?.getUserById(it.userId)!!))
            }
            handler.sendEmptyMessage(0)
        }
    }

    private fun covertUserToEntity(user: User): com.example.gitstarscounter.entity.user.User {
        return com.example.gitstarscounter.entity.user.User(
            user.id,
            user.login,
            user.avatarUrl
        )
    }

    private fun covertRepositoryToEntity(): com.example.gitstarscounter.entity.repository.Repository {
        return com.example.gitstarscounter.entity.repository.Repository(
            id = repository.id,
            name = repository.name,
            userId = repository.user.id
        )
    }

    private fun covertEntityToRepository(
        repository: com.example.gitstarscounter.entity.repository.Repository,
        user: com.example.gitstarscounter.entity.user.User
    ): Repository {
        return Repository(
            id = repository.id,
            name = repository.name.toString(),
            allStarsCount = 0,
            user = convertEntityToUser(user)
        )
    }


    private fun convertEntityToUser(user: com.example.gitstarscounter.entity.user.User): User {
        return User(user.id, user.name!!, user.avatarUrl)
    }

    @SuppressLint("SimpleDateFormat")
    private fun covertEntityToStar(
        star: com.example.gitstarscounter.entity.star.Star,
        user: com.example.gitstarscounter.entity.user.User
    ): Star {
        return Star(sdf3.parse(star.starredAt), convertEntityToUser(user))
        // val lol = SimpleDateFormat("dd/MM/yyyy").parse(star.starredAt)
    }

    private fun convertStarToEntity(
        star: Star,
        repositoryId: Long
    ): com.example.gitstarscounter.entity.star.Star {
        /* Log.d("USER_ID",  star.user.id.toString())
         Log.d("REP_ID",  repository.id.toString())*/
        return com.example.gitstarscounter.entity.star.Star(
            starredAt = star.starredAt.toString(),
            repositoryId = repositoryId,
            userId = star.user.id
        )
    }

    companion object {
        private const val NUMBER_OF_THREADS = 4
        var sdf3: SimpleDateFormat =
            SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
    }
}
