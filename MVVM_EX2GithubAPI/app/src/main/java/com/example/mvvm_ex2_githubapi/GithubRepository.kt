package com.example.mvvm_ex2_githubapi

import com.example.mvvm_ex2_githubapi.di.GithubService

class GithubRepository {
    private val githubClient = GithubService.client

    suspend fun getRepositories(query: String) = githubClient?.getRepositories(query)
}