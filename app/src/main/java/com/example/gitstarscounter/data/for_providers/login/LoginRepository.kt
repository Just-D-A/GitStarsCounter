package com.example.gitstarscounter.data.for_providers.login

import android.util.Log
import com.example.gitstarscounter.data.to_rename_2.local.providers.LocalLoginProvider
import com.example.gitstarscounter.data.to_rename_2.remote.RequestLimit
import com.example.gitstarscounter.data.to_rename_2.remote.entity.resource_remote.ResourceRemote
import com.example.gitstarscounter.data.to_rename_2.remote.providers.RemoteLoginProvider
import com.example.gitstarscounter.entity.Repository
import com.omega_r.base.errors.AppException
import com.omega_r.base.errors.throwNoData
import java.io.IOException

open class LoginRepository {
    companion object {
        const val TAG = "LoginRepository"
    }

    private val remoteLoginProvider = RemoteLoginProvider()
    private val localLoginProvider = LocalLoginProvider()


    suspend fun getRemoteUsersRepositories(
        userName: String,
        pageNumber: Int
    ): List<Repository>? {
        return try {
            Log.d(TAG, "Try GET")
            if(!RequestLimit.hasRequest()) {
                throw IOException()
            }
            remoteLoginProvider.getUsersRepositories(userName, pageNumber)
        } catch (exception: IOException) {
            Log.d(TAG, "CATCH EXC")
            try {
                localLoginProvider.getUsersRepositories(userName, 0)
            } catch (e: AppException.NoData) {
                throwNoData()
            }
        }
    }

    suspend fun getLimitRemaining(): ResourceRemote? {
        return remoteLoginProvider.getLimitRemaining()
    }

    suspend fun loadMoreRepositories(userName: String, pageNumber: Int): List<Repository>? {
        return remoteLoginProvider.loadMoreRepositories(userName, pageNumber)
    }

/*    suspend fun getLocalUsersRepositories(
        userName: String
    ): List<Repository>? {
        return localLoginProvider.getUsersRepositories(userName, 0)
    }*/
}