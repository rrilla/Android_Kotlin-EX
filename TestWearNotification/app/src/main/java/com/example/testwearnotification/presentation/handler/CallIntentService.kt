package com.example.testwearnotification.presentation.handler

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CallIntentService : JobIntentService() {
    private val TAG = this.javaClass.simpleName

    companion object {
        val ACTION_DISMISS = "com.example.testwearnotification.presentation.handler.action.DISMISS"
        val ACTION_ACCEPT = "com.example.testwearnotification.presentation.handler.action.ACCEPT"
    }

    override fun onHandleWork(intent: Intent) {
        Log.e(TAG, "onHandleWork(): $intent")

        intent.action?.let {
            when (it) {
                ACTION_ACCEPT -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.e("HJH", "수락 - API 호출")
                    }
                }
                else -> {
                    Log.e("HJH", "거절 알림 종료.")
                }
            }
        }
    }
}