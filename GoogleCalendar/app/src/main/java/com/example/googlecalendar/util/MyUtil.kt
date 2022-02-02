package com.example.googlecalendar.util

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.googlecalendar.R
import java.text.SimpleDateFormat
import java.util.*

fun myCheckPermission(activity: AppCompatActivity, permissionName: String): Boolean {
    return if (ContextCompat.checkSelfPermission(
            activity,
            permissionName
        ) == PackageManager.PERMISSION_GRANTED){
        true
    }else{
        myRequestPermission(activity, permissionName)
        false
    }
}

fun myRequestPermission(activity: AppCompatActivity, permissionName: String) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
        activity.shouldShowRequestPermissionRationale(permissionName)){
        Log.e("han","2")
        AlertDialog.Builder(activity).run {
//                        setTitle("위치권한확인")
            setIcon(android.R.drawable.ic_dialog_alert)
            setMessage(R.string.suggest_permissison_grant_in_setting)
            setPositiveButton("확인"){
                    _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", activity.packageName, null)
                activity.startActivity(intent)
            }
            setNegativeButton("취소", null)
            show()
        }
    }else{
        Log.e("han","3")
        val requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                Toast.makeText(activity, "권한 승인", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "권한 거부", Toast.LENGTH_SHORT).show()
            }
        }
        requestPermissionLauncher.launch(permissionName)
    }
}

//fun myCheckPermission2(activity: AppCompatActivity): Boolean {
//    return when {
//        ContextCompat.checkSelfPermission(
//            activity,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED -> {
//            Log.e("han","1")
//            true
//        }
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
//            Log.e("han","2")
//            AlertDialog.Builder(activity).run {
////                        setTitle("위치권한확인")
//                setIcon(android.R.drawable.ic_dialog_alert)
//                setMessage(R.string.suggest_permissison_grant_in_setting)
//                setPositiveButton("확인"){
//                        _, _ ->
//                    val intent = Intent()
//                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                    intent.data = Uri.fromParts("package", activity.packageName, null)
//                    activity.startActivity(intent)
//                }
//                setNegativeButton("취소", null)
//                show()
//            }
//            false
//        }
//        else -> {
//            Log.e("han","3")
//            val requestPermissionLauncher = activity.registerForActivityResult(
//                ActivityResultContracts.RequestPermission()
//            ) {
//                if (it) {
//                    Toast.makeText(activity, "권한 승인", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(activity, "권한 거부", Toast.LENGTH_SHORT).show()
//                }
//            }
//            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//            false
//        }
//    }
//}

fun dateToString(date: Date): String {
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(date)
}