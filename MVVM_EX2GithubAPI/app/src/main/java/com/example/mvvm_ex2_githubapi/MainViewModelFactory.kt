package com.example.mvvm_ex2_githubapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/***
 *  생성자에 파라미터가 있는 ViewModel들은 ViewModelFactory를 작성하고 이를 통해 초기화 해야 한다.
 */
class MainViewModelFactory(private val githubRepository: GithubRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GithubRepository::class.java).newInstance(githubRepository)
    }
}
