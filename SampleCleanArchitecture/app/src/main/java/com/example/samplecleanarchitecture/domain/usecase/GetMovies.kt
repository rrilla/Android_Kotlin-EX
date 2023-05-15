package com.example.samplecleanarchitecture.domain.usecase

import com.example.samplecleanarchitecture.core.interactor.UseCase
import com.example.samplecleanarchitecture.domain.model.Movie
import com.example.samplecleanarchitecture.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMovies
@Inject constructor(private val moviesRepository: MoviesRepository) : UseCase<List<Movie>, UseCase.None>() {

    override suspend fun run(params: None) = moviesRepository.movies()
}