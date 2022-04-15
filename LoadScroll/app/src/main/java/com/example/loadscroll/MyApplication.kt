package com.example.loadscroll

import android.app.Application
import com.example.loadscroll.data.NetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication: Application() {

    companion object {
        val API_KEY = BuildConfig.GIPHY_API_KEY
        val BASE_URL = "https://api.giphy.com"


        //add....................................
        var networkService: NetworkService
        val retrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        init {
            networkService = retrofit.create(NetworkService::class.java)
        }
    }
}