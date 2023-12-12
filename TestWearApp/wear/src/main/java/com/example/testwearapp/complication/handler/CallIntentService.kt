package com.example.testwearapp.complication.handler

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CallIntentService : IntentService("CallIntentService") {

    private val TAG = this.javaClass.simpleName

    companion object {
        val ACTION_DISMISS = "com.example.testwearapp.complication.handler.action.DISMISS"
        val ACTION_ACCEPT = "com.example.testwearapp.complication.handler.action.ACCEPT"

        val ARG_PARAM_ID = "param_id"
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.e("HJH", "onBind()")
        return super.onBind(intent)
    }

    override fun onHandleIntent(p0: Intent?) {
        Log.e(TAG, "onHandleWork(): $p0")

        p0?.action?.let {
            val notificationId = p0.getIntExtra(ARG_PARAM_ID, 0)
//            p0.extras?.keySet()?.forEach {
//                Log.e("")
//            }
            when (it) {
                ACTION_ACCEPT -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.e("HJH", "수락 - API 호출")
                    }
                }
                else -> {
                    Log.e("HJH", "거절 알림 종료.")
                    handleActionDismiss(notificationId)
                }
            }
        }
    }

    private fun handleActionAccept(id: Int) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.cancel(id)
    }

    private fun handleActionDismiss(id: Int) {
        Log.e("HJH", "id : $id")
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.cancel(id)
    }
}