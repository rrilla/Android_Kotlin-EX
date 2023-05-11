package com.example.cleanarchitecture.view.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cleanarchitecture.core.platform.BaseViewModel
import com.example.domain.usecase.movie.GetMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    val getMovies: GetMovies
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiState2 = MutableLiveData(HomeUiState())
    val uiState2: LiveData<HomeUiState> = _uiState2



    fun loadMovies() {
    }


}