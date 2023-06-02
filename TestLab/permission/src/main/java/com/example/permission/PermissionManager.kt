package com.example.permission

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

class PermissionManager @Inject constructor(
    private val activity: AppCompatActivity
) {

    enum class PermissionMember {
        POST_NOTIFICATIONS,
        WRITE_EXTERNAL_STORAGE,
        CAMERA
    }


    fun test() {
        Log.e("HJH", "test() 호출")
    }


    //  권한 체크 및 성공 시 기능 수행
    //  param : 요청권한, 성공시, 실패

    //
}