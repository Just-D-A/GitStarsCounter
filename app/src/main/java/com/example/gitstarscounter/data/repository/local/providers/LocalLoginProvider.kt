@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gitstarscounter.data.repository.local.providers

import android.util.Log
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.data.providers.login.LoginProvider
import com.omega_r.base.errors.throwNoData

@Suppress("NAME_SHADOWING")
class LocalLoginProvider : LoginProvider {
    companion object {
        private const val TAG = "LoginLocalProvider"
    }

    private val database = GitStarsApplication.instance.gitStarsCounterComponent.getRoomDatabase()
    private val userTable = database.userDao()
    private val repositoryTable = database.repositoryDao()

    override suspend fun getUsersRepositories(userName: String, pageNumber: Int): List<Repository> {
        val user = userTable.getUserByName(userName) //query to DB

        Log.d(TAG, "${user?.name} getted")

        if (user == null) {
            throwNoData()
        }

        user.let { user ->
            return repositoryTable.getRepositoriesByUserId(user.id)
        }
    }
}
