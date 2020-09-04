package com.example.gitstarscounter.service

import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.git_api.StarModel

interface ServiceCallback {
    fun onDatabaseRepositoryResponse(repositoryModelList: List<RepositoryModel>)
    fun onStarsResponse(
        responseStarsList: MutableList<StarModel>,
        repositoryModel: RepositoryModel,
        pageNumber: Int
    )
    fun onError(textResource: Int)
    fun onFindStarResponse(newStars: MutableList<StarModel>, repositoryModel: RepositoryModel)
}