package com.example.gitstarscounter.data.providers.login

import android.util.Log
import com.example.gitstarscounter.data.repository.remote.exception.LimitException
import com.example.gitstarscounter.data.repository.local.providers.LocalLoginProvider
import com.example.gitstarscounter.data.repository.remote.RequestLimit
import com.example.gitstarscounter.data.repository.remote.entity.resource_remote.ResourceRemote
import com.example.gitstarscounter.data.repository.remote.providers.RemoteLoginProvider
import com.example.gitstarscounter.entity.Repository
import com.omega_r.base.errors.AppException
import com.omega_r.base.errors.throwNoData

open class LoginRepository {
    companion object {
        private const val TAG = "LoginRepository"
    }

    private val remoteLoginProvider = RemoteLoginProvider()
    private val localLoginProvider = LocalLoginProvider()

    suspend fun getUsersRepositories(userName: String, pageNumber: Int): List<Repository> {
        return try {
            if (!RequestLimit.hasRequest()) {
                throw LimitException() // create new LimitException
            }
            remoteLoginProvider.getUsersRepositories(userName, pageNumber)
        } catch (exception: Exception) {
            try {
                localLoginProvider.getUsersRepositories(userName, 0)
            } catch (e: AppException.NoData) {
                Log.d(TAG, "catch NoData exception")
                throwNoData()
            }
        } catch (e: LimitException) {
            Log.d(TAG, "catch Limit exception")
            try {
                localLoginProvider.getUsersRepositories(userName, 0)
            } catch (e: AppException.NoData) {
                Log.d(TAG, "catch NoData exception")
                throwNoData()
            }
        }
    }

    suspend fun getMoreUsersRepositories(userName: String, pageNumber: Int): List<Repository> {
        return try {
            if (!RequestLimit.hasRequest()) {
                throw LimitException() // create new LimitException
            }
            remoteLoginProvider.getUsersRepositories(userName, pageNumber)
        } catch (e: Exception) {
            throwNoData()
        } catch (e: LimitException) {
            throwNoData()
        }
    }
}
