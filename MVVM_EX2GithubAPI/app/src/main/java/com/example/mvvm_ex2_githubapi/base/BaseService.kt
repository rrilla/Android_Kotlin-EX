package com.example.mvvm_ex2_githubapi.base

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaseService {
    fun getClient(baseUrl: String): Retrofit? = Retrofit.Builder()
        .baseUrl(baseUrl)
//        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}