package com.example.mvvm_ex2_githubapi.di

import com.example.mvvm_ex2_githubapi.base.BaseService
import com.example.mvvm_ex2_githubapi.data.remote.api.GithubApi

//   Singletone으로 관리
object GithubService {
    private const val GITHUB_URL = "https://api.github.com"

    val client = BaseService().getClient(GITHUB_URL)?.create(GithubApi::class.java)
}