package com.example.gitstarscounter.data.repository.remote

import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.data.providers.login.LoginSearch
import com.example.gitstarscounter.data.providers.star.StarsSearch
import javax.inject.Inject

class SearchProvider {
    @Inject
    lateinit var gitApiService: GithubApiService

    init {
        GitStarsApplication.instance.gitStarsCounterComponent.inject(this)
    }

    fun provideSearchRepository(): LoginSearch {
        return LoginSearch(gitApiService)
    }

    fun provideSearchStars(): StarsSearch {
        return StarsSearch(gitApiService)
    }
}
