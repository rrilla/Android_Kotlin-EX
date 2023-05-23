package com.example.samplecleanarchitecture.view.main.home

import com.example.samplecleanarchitecture.domain.model.Movie
import com.example.samplecleanarchitecture.domain.model.MovieDetails

data class HomeUiState(
    val dd: Boolean = false,
    val movies : List<Movie> = listOf(),
    val movieDetail : MovieDetails = MovieDetails.empty
)