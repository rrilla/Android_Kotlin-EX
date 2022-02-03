package com.example.mvvm_ex2_githubapi.data.remote.api

import com.example.mvvm_ex2_githubapi.data.remote.model.GithubRepositoriesModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/repositories")
    //  suspend는 Coroutine을 위해 사용하는 키워드
    suspend fun getRepositories(
        @Query("q") query: String
    ): Response<GithubRepositoriesModel>
}