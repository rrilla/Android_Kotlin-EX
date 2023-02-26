package com.example.test_notification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("HJH", "onCreate()")
        savedInstanceState?.keySet()?.forEach {
            Log.e("HJH", "bundle : $it")
        }

        // notification 키 추가한 알림 클릭시 data 값 존재.
        intent.extras?.keySet()?.forEach {
            Log.e("HJH", "intent : $it")
        }

//        Log.e("HJH", intent.getStringExtra("zz").toString())
    }

    //  액티비티가 새로 생성되지 않고 사용 중인 상황에서 intent를 받기 위한 메서드
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("HJH", "onNewIntent()")

        intent?.extras?.keySet()?.forEach {
            Log.e("HJH", "intent : $it")
        }
    }
}