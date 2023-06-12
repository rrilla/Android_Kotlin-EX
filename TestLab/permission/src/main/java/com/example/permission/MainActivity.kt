package com.example.permission

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button1).setOnClickListener {
            permissionManager.checkRequestPermission(PermissionManager.PermissionMember.POST_NOTIFICATIONS) {
                Log.e("HJH", "알림 기능 ㄱㄱ")
            }
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            permissionManager.checkRequestPermission(PermissionManager.PermissionMember.WRITE_EXTERNAL_STORAGE) {
                Log.e("HJH", "쓰기 기능 ㄱㄱ")
            }
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            permissionManager.checkRequestPermission(PermissionManager.PermissionMember.CAMERA) {
                Log.e("HJH", "카메라 기능 ㄱㄱ")
            }
        }
    }
}