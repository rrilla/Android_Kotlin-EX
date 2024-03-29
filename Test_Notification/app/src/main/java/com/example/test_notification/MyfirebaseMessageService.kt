package com.example.test_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyfirebaseMessageService: FirebaseMessagingService() {
    val TAG = this::class.java.name

    /**
     * 토큰 생성 모니터링. 아래와 같을 때 재생성 됨.
     * - 새 기기에서 앱 복원
     * - 사용자가 앱 제거/재설치
     * - 사용자가 앱 데이터 소거
     * */
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.e(TAG, "fcm token = $token")
    }

    //  모든 메시지는 수신된 지 20초(Android Marshmallow의 경우 10초) 이내에 처리되어야 함. 넘으면 Workmanger or Job Schduler 사용
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e(TAG, "fcm message : ${message.data}")
        Log.e(TAG, "fcm notification : channelId : ${message.notification?.channelId} \n body : ${message.notification?.body}")

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = getChannelBuilder(manager)

        builder.run {
            //  알림의 기본 정보
            setSmallIcon(R.drawable.ic_stat_ic_notification)
//            setWhen(System.currentTimeMillis())
            setContentTitle("한재현")
            setContentText("ㅎㅇㅎㅇ")
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_stat_ic_notification))
        }

        //  원격 입력 설정 - BroadcastReceiver 등록 필요.
        val KEY_TEXT_REPLY = "key_text_reply"   //  RemoteInput의 입력을 식별하는 값
        var replyLabel: String = "답장"  //  입력 힌트
        var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }
        val replyIntent = Intent(this, ReplyReceiver::class.java)
        val replyPendingIntent = PendingIntent.getBroadcast(
            this, 30, replyIntent, PendingIntent.FLAG_MUTABLE
        )
        builder.addAction(
            NotificationCompat.Action.Builder(
                R.drawable.send,
                "답장",
                replyPendingIntent
            ).addRemoteInput(remoteInput).build()
        )


        //  content 클릭 이벤트 설정
        val intent = Intent(this, MainActivity::class.java).run {
            putExtra("zz", "hi")
            // 동작 정의 필요. RemoteMessage 전달하거나, flag 추가 등
        }
        val dd = PendingIntent.getActivity(this, 50, intent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(dd)


        manager.notify(11, builder.build())
    }

    private fun getChannelBuilder(manager: NotificationManager): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //  26버전 이상
            val channelId = "one-channel"
            val channelName = "JaeHyeon`s Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "JH`s Channel One Description"    //  채널 설명

                setShowBadge(true)  //  홈 화면 뱃지 아이콘 출력 여부 - 미확인 알림갯수 표시


                // 사운드 설정
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)

                //  진동 설정
                enableVibration(true)   //  진동 울림 여부
//                    //  진동 패턴
//                    vibrationPattern = longArrayOf(100, 200, 300)
//                    //  불빛 표시 여부
//                    enableLights(true)
//                    //  불빛 색상
//                    lightColor = Color.RED
            }

            manager.createNotificationChannel(channel)
            NotificationCompat.Builder(this, channelId)
        } else {
            //  26버전 이하
            NotificationCompat.Builder(this)
        }
    }
}