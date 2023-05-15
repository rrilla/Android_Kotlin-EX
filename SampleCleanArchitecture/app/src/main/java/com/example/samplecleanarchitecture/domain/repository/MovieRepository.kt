package com.example.samplecleanarchitecture.domain.repository

import com.example.samplecleanarchitecture.core.exception.Failure
import com.example.samplecleanarchitecture.core.functional.Either
import com.example.samplecleanarchitecture.core.functional.Either.Left
import com.example.samplecleanarchitecture.core.functional.Either.Right
import com.example.samplecleanarchitecture.core.platform.NetworkHandler
import com.example.samplecleanarchitecture.data.service.MoviesService
import com.example.samplecleanarchitecture.domain.model.Movie
import retrofit2.Call
import javax.inject.Inject

interface MoviesRepository {
    fun movies(): Either<Failure, List<Movie>>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val service: MoviesService
    ) : MoviesRepository {

        override fun movies(): Either<Failure, List<Movie>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> request(
                    service.movies(),
                    { it.map { movieEntity -> movieEntity.toMovie() } },
                    emptyList()
                )
                false -> Left(Failure.NetworkConnection)
            }
        }

        private fun <T, R> request(
            call: Call<T>,
            transform: (T) -> R,
            default: T
        ): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Right(transform((response.body() ?: default)))
                    false -> Left(Failure.ServerError)
                }
            } catch (exception: Throwable) {
                exception.printStackTrace()
                Left(Failure.ServerError)
            }
        }
    }
}