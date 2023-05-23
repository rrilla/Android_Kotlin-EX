package com.example.samplecleanarchitecture.domain.usecase

import com.example.samplecleanarchitecture.core.interactor.UseCase
import com.example.samplecleanarchitecture.domain.model.Movie
import com.example.samplecleanarchitecture.domain.model.MovieDetails
import com.example.samplecleanarchitecture.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMovieDetails
@Inject constructor(private val moviesRepository: MoviesRepository) : UseCase<MovieDetails, GetMovieDetails.Params>() {

    override suspend fun run(params: Params) = moviesRepository.movieDetails(params.id)

    data class Params(val id: Int)
}