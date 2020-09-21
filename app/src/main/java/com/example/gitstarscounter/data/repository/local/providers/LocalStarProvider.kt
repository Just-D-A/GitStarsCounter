@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.data.repository.local.providers

import android.util.Log
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.repository.local.entity.LocalStar
import com.example.gitstarscounter.data.repository.local.entity.LocalUser
import com.example.gitstarscounter.data.repository.local.entity.database.repository.TableRepository
import com.example.gitstarscounter.data.repository.local.entity.database.star.TableStar
import com.example.gitstarscounter.data.repository.remote.entity.RemoteRepository
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.data.providers.star.StarProvider

class LocalStarProvider(
) : StarProvider {
    companion object {
        private const val TAG = "StarLocalProvider"
    }

    private val database = GitStarsApplication.instance.database
    private val userTable = database.userDao()
    private val repositoryTable = database.repositoryDao()
    private val starTable = database.starDao()

    suspend fun insertToDatabase(starList: List<Star>, repositoryRemote: RemoteRepository) {
        userTable.insertAll(LocalUser(repositoryRemote.user))
        repositoryTable.insertAll(TableRepository(repositoryRemote))

        val list = repositoryTable.getAll()
        list.forEach { repositoryFromDatabase ->
            Log.d(TAG, "REP_NAME ${repositoryFromDatabase.name}")
        }

        starList.forEach { remoteStar ->
            val star = LocalStar(remoteStar, repositoryRemote)

            userTable.insertAll(LocalUser(remoteStar.user))

            val starFromDB =
                starTable.findByRepositoryUserAndId(star.repository.id, star.user.id)
            if (starFromDB == null) {
                starTable.insertAll(TableStar(star))
                Log.d(TAG, "add new star to DB user_id ${star.user.id} and rep_id ${star.repository.id}")
            }
        }

    }

    suspend fun checkUnstar(starsListFromApi: List<Star>, repositoryRemote: Repository) {
        //get all stars from db buy rep id
        val starsListFromDB = starTable.findByRepositoryId(repositoryRemote.id)

        //create map by key star.user with second param star-api
        val starsMap = mutableMapOf<Long, TableStar>()
        starsListFromApi.forEach {
            val star = TableStar(LocalStar(it, repositoryRemote))
            starsMap[star.userId] = star
        }

        /*for each star from db find star from api by id_user
        if not find -> delete star from db*/
        var i = 0
        starsListFromDB.forEach {
            val starFromApi = starsMap[it.userId]
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
    ): List<Star>? {
        val starsTypeList: MutableList<Star> = mutableListOf()

        val starsList = starTable.findByRepositoryId(repositoryRemote.id)
        starsList.forEach {
            val localUser = userTable.getUserById(it.userId)
            starsTypeList.add(
                LocalStar(it, repositoryRemote, localUser)
            )
        }
        return starsTypeList
    }
}
