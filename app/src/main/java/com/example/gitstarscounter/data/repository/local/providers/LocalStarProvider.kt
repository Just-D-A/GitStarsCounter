@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.data.repository.local.providers

import android.util.Log
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.repository.local.entity.LocalStar
import com.example.gitstarscounter.data.repository.local.entity.LocalUser
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.data.providers.star.StarProvider
import com.example.gitstarscounter.data.repository.local.entity.LocalRepository

class LocalStarProvider : StarProvider {
    companion object {
        private const val TAG = "StarLocalProvider"
    }

    private val database = GitStarsApplication.instance.appRoomDatabase
    private val userTable = database.userDao()
    private val repositoryTable = database.repositoryDao()
    private val starTable = database.starDao()

    suspend fun insertToDatabase(starList: List<Star>, repository: Repository) {
        Log.d(TAG, "Start ADDED USER")

        userTable.insertAll(LocalUser(repository.user))
        repositoryTable.insertAll(LocalRepository(repository))

        starList.forEach { remoteStar ->

            val star = LocalStar(remoteStar, repository)

            Log.d(TAG, "Convert ADDED USER")

            userTable.insertAll(LocalUser(remoteStar.user))

            Log.d(TAG, "ADDED USER")

            val starFromDB =
                starTable.findByRepositoryUserAndId(star.repository.id, star.user.id)
            if (starFromDB == null) {
                starTable.insertAll(star)
                Log.d(TAG, "add star ${star.user.id} and rep_id ${star.repository.id}")
            }
        }
    }

    suspend fun checkUnstar(starsListFromApi: List<Star>, repository: Repository) {
        Log.d(TAG, "START DELETED STARS")
        //get all stars from db buy rep id
        val starsListFromDB = starTable.findByRepositoryId(repository.id)

        //create map by key star.user with second param star-api
        val starsMap = mutableMapOf<Long, LocalStar>()
        starsListFromApi.forEach {
            val star = LocalStar(it, LocalRepository(repository))
            starsMap[star.user.id] = star
        }

        Log.d(TAG, "CONT DELETED STARS")
        /*for each star from db find star from api by id_user
        if not find -> delete star from db*/
        var i = 0
        starsListFromDB.forEach {
            val starFromApi = starsMap[it.user.id]
            if (starFromApi == null) {
                starTable.delete(it)
                i++
            }
        }
        Log.d(TAG, "DELETED $i STARS")
    }

    override suspend fun getRepositoryStars(
        userName: String,
        repositoryRemote: Repository,
        pageNumber: Int
    ): List<Star> {
        val starsTypeList: MutableList<Star> = mutableListOf()

        val starsList = starTable.findByRepositoryId(repositoryRemote.id)
        starsList.forEach {
            starsTypeList.add(
                LocalStar(it, LocalRepository(repositoryRemote), it.user)
            )
        }
        return starsTypeList
    }
}
