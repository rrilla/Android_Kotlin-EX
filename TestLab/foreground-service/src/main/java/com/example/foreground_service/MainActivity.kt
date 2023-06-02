package com.example.foreground_service

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foreground_service.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        const val  ACTION_STOP = ".stop"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListeners()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS),0)
    }

    private fun setButtonListeners() {
        binding.btnStart.setOnClickListener {
            startBasicService()
        }
        binding.btnStop.setOnClickListener {
            stopBasicService()
        }
    }

    private fun startBasicService() {
        Intent(this, SampleForegroundService::class.java).run {
//            action = ACTION_STOP
            if (Build.VERSION.SDK_INT > 26) startForegroundService(this)
            else startService(this)
        }
    }

    private fun stopBasicService() {
        Intent(this, SampleForegroundService::class.java).run {
            stopService(this)
        }
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        try {
            val manager =
                getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(
                Int.MAX_VALUE
            )) {
                Log.e("HJH", "${serviceClass.name}")
                Log.e("HJH", "${service.service.className}")
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}