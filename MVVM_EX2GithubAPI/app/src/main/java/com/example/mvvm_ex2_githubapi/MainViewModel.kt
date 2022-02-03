package com.example.mvvm_ex2_githubapi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_ex2_githubapi.model.GithubRepositoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val githubRepository: GithubRepository): ViewModel() {
    private val _githubRepositories = MutableLiveData<List<GithubRepositoryModel>>()
    val githubRepositories = _githubRepositories

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