package com.example.gitstarscounter.data.local.providers

import android.util.Log
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.local.database.enitity.star.TableStar
import com.example.gitstarscounter.data.local.entity.RepositoryLocal
import com.example.gitstarscounter.data.local.entity.StarLocal
import com.example.gitstarscounter.data.remote.entity.StarRemote
import com.example.gitstarscounter.entity.RepositoryModel
import com.example.gitstarscounter.entity.StarModel

class ServiceLocalProvider {
    private val database = GitStarsApplication.instance?.database!!
    private val repositoryDao = database.repositoryDao()
    private val starDao = database.starDao()
    private val repositoryModelList =
        mutableListOf<com.example.gitstarscounter.entity.RepositoryModel>()

    fun getAllDatabaseRepositories(): List<com.example.gitstarscounter.entity.RepositoryModel> {
        val repositoryEntityList = repositoryDao?.getAll()
        Log.d("SERVICE FB OBJECT", repositoryEntityList?.size.toString())
        repositoryEntityList?.forEach {
            repositoryModelList.add(RepositoryLocal(it))
        }
        return repositoryModelList
    }

    fun findNewStars(
        starListFromApi: List<StarRemote>,
        repository: RepositoryModel
    ): MutableList<StarModel> {
        val newStars = mutableListOf<StarModel>()

        starListFromApi.forEach { starFromApi -> //стоит ли так делать или лучше it??
            val starFromDB =
                starDao?.findByRepositoryUserAndId(repository.id, starFromApi.user.id)
            if (starFromDB == null) {
                Log.d("ServiceProvider", "add NEW star")
                newStars.add(starFromApi)
                starDao?.insertAll(
                    TableStar(StarLocal(starFromApi, repository))
                )
            }
        }

        return newStars
    }
}
