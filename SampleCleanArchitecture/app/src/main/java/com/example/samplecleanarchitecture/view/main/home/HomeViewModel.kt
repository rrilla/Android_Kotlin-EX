package com.example.samplecleanarchitecture.view.main.home

import androidx.lifecycle.viewModelScope
import com.example.samplecleanarchitecture.core.interactor.UseCase.None
import com.example.samplecleanarchitecture.core.platform.BaseViewModel
import com.example.samplecleanarchitecture.domain.model.Movie
import com.example.samplecleanarchitecture.domain.usecase.GetMovieDetails
import com.example.samplecleanarchitecture.domain.usecase.GetMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val getMovies : GetMovies,
    private val getMovieDetails: GetMovieDetails,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadMovies() = getMovies(None(), viewModelScope, ::handleLoading) {
        it.fold(::handleFailure) {
            _uiState.update { uiState ->
                uiState.copy(movies = it)
            }
        }
    }

    fun loadMovieDetails(movieId: Int) = getMovieDetails(GetMovieDetails.Params(movieId), viewModelScope, ::handleLoading) {
        it.fold(::handleFailure) {
            _uiState.update { uiState ->
                uiState.copy(movieDetail = it)
            }
        }
    }
}