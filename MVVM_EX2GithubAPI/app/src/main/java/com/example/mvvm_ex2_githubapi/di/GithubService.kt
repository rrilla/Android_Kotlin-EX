package com.example.mvvm_ex2_githubapi.di

import com.example.mvvm_ex2_githubapi.base.BaseService
import com.example.mvvm_ex2_githubapi.data.remote.api.GithubApi

//   Singletone으로 관리
object GithubService : BaseService() {
    override val baseUrl = "https://api.github.com"

//    suspend fun getRepositories(query: String){
//        getClient()?.create(GithubApi::class.java)?.getRepositories(query)
//    }

//    private val GITHUB_URL = "https://api.github.com"

    val client = getClient()?.create(GithubApi::class.java)


}