package com.example.gitstarscounter.data.repository.remote

import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.providers.login.LoginSearch
import com.example.gitstarscounter.data.providers.star.StarsSearch

object SearchProvider {
    private val gitApiService = GitStarsApplication.instance.gitApi
/*  maybe this is right??
    @Inject
    lateinit var gitApiService2: GithubApiService
*/
    fun provideSearchRepository(): LoginSearch {
        return LoginSearch(gitApiService)
    }

    fun provideSearchStars(): StarsSearch {
        return StarsSearch(gitApiService)
    }
}
