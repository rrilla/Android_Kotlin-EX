package com.example.mvvm_ex2_githubapi.base

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseService {

    abstract val baseUrl: String

    fun getClient(): Retrofit? = Retrofit.Builder()
        .baseUrl(baseUrl)
//        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}