package com.example.testwearapp.complication

import android.app.Application

class App:Application() {

    companion object{
        lateinit var application: App
        fun get() : App = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}