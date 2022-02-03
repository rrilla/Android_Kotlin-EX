package com.example.mvvm_ex2_githubapi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_ex2_githubapi.GithubRepository
import com.example.mvvm_ex2_githubapi.data.remote.model.GithubRepositoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class MainViewModel(private val githubRepository: GithubRepository): ViewModel() {
class MainViewModel(): ViewModel() {
    private val _githubRepositories = MutableLiveData<List<GithubRepositoryModel>>()
    val githubRepositories = _githubRepositories
    private val githubRepository = GithubRepository()

    fun requestGithubRepositories(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            githubRepository.getRepositories(query)?.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let {
                        _githubRepositories.postValue(it.items)
                    }
                }
            }
        }
    }
}