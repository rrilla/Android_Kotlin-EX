package com.example.samplecleanarchitecture.view.main.permission

import com.example.samplecleanarchitecture.domain.model.Movie
import com.example.samplecleanarchitecture.domain.model.MovieDetails

data class PermissionUiState(
    val dd: Boolean = false,
    val movies : List<Movie> = listOf(),
    val movieDetail : MovieDetails = MovieDetails.empty
)