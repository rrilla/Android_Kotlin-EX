package com.example.samplecleanarchitecture.data.service

import com.example.samplecleanarchitecture.data.MoviesApi
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesService
@Inject constructor(retrofit: Retrofit) : MoviesApi {
    private val moviesApi by lazy { retrofit.create(MoviesApi::class.java) }

    override fun movies() = moviesApi.movies()
}