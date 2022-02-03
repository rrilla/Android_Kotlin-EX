package com.example.mvvm_ex2_githubapi

//   Singletone으로 관리
object GithubService {
    private const val GITHUB_URL = "https://api.github.com"
    val client = BaseService().getClient(GITHUB_URL)?.create(GithubApi::class.java)
}