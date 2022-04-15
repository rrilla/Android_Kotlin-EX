package com.example.loadscroll

import android.app.Application
import androidx.room.Room
import com.example.loadscroll.data.NetworkService
import com.example.loadscroll.data.db.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication: Application() {

    companion object {
        val BASE_URL = "https://api.giphy.com"
        lateinit var dataBase: AppDatabase

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

    override fun onCreate() {
        super.onCreate()

        dataBase = Room.databaseBuilder(
            baseContext,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }
}