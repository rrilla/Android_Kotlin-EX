package com.example.foreground_service

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.foreground_service.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionBinding

    val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        it.forEach { item ->
            Log.e("HJH", "${item.key}  - ${item.value}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)




        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE).also { Log.e("HJH", "READ 체크 - $it") }
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).also { Log.e("HJH", "WRITE 체크 - $it") }
        checkPermission(Manifest.permission.READ_MEDIA_VIDEO).also { Log.e("HJH", "read_media_video 체크 - $it") }
        checkPermission(Manifest.permission.READ_MEDIA_IMAGES).also { Log.e("HJH", "READ_MEDIA_IMAGES 체크 - $it") }

        requestPermission(arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_MEDIA_VIDEO,
//            Manifest.permission.READ_MEDIA_IMAGES,
        ))
//        Manifest.permission.READ_MEDIA_IMAGES


//        Manifest.permission.WRITE_EXTERNAL_STORAGE // 이권한 명시적 요청임.
    }


    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permissions: Array<String>) {
        launcher.launch(permissions)
    }
}