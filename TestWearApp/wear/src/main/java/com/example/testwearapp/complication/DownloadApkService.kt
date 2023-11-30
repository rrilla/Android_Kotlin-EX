package com.example.testwearapp.complication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

// TODO: 필요한 파일, 확장자 추가
enum class FileType(val extension: String) {
    APK(".apk"),
    VIDEO(".mp4")
}

class DownloadApkService : JobIntentService() {


    private val NOTIFICATION_CHANNEL_ID = "notification_download"
    private val NOT_NOTIFICATION_CHANNEL_ID = "not_notification_download"

    private val NOTIFICATION_ID = 123
    private val NOTIFICATION_CHANNEL_NAME = "파일 다운로드"
    private val NOTIFICATION_CHANNEL_DESCRIPTION = "notificationDescription"
    private val PANDING_REQUEST_CODE = 1234

    private lateinit var outputDirectory: File
    private lateinit var installer: PackageInstaller
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    private lateinit var fileType: String
    private lateinit var fileExtension: String

    private var notificationImportance: Int = 0
    private var isNotification: Boolean = false

    private val fileOn = "파일 다운로드 (ON)"
    private val fileOff = "파일 다운로드 (OFF)"

    companion object {
        private val rootDir = "test"

        lateinit var apkUrl: String

        private var onDownloadProgressBar: OnDownloadProgressBar? = null
        fun enqueueWork(context: Context, work: Intent, onDownloadProgressBar: OnDownloadProgressBar? = null) {
            Companion.onDownloadProgressBar = onDownloadProgressBar
            enqueueWork(context, DownloadApkService::class.java, 121, work)
        }

        fun fileDir(fileType: String): File{
            // 앱 내부 캐시 폴더에 저장
            App.get().applicationContext.apply {
                val mediaDir = externalCacheDirs.firstOrNull()?.let {
                    File(it, "$rootDir/$fileType").apply { mkdirs() }
                }
                return if (mediaDir != null && mediaDir.exists())
                    mediaDir else filesDir
            }
        }
    }

    override fun onHandleWork(intent: Intent) {
        fileType = intent.getStringExtra(Const.FILE_TYPE).toString()
        fileExtension = intent.getStringExtra(Const.FILE_EXTENSION).toString()
        isNotification = intent.getBooleanExtra(Const.FILE_NOTIFICATION, false)


        apkUrl = intent.getStringExtra(Const.APK_EXTRA).toString()
        installer = packageManager.packageInstaller

        val channelId: String
        val channelName: String
        // 알림 채널 설정
        if (isNotification) {
            notificationImportance = NotificationManager.IMPORTANCE_DEFAULT
            channelId = NOTIFICATION_CHANNEL_ID
            channelName = fileOn
        } else {
            notificationImportance = NotificationManager.IMPORTANCE_NONE
            channelId = NOT_NOTIFICATION_CHANNEL_ID
            channelName = fileOff
        }

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        setNotification(channelId, channelName)

        // 다운로드 폴더 만들기
        outputDirectory = getOutputDirectory()

        try {
            CoroutineScope(Dispatchers.IO).launch {
                // 리스트에 주소 넣어서 다운
                when (fileType) {
                    FileType.APK.toString() -> {
                        downloadFile(apkUrl)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun setNotification(channelId: String, channelName: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            notificationImportance
        )
        notificationChannel.apply {
            description = NOTIFICATION_CHANNEL_DESCRIPTION
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(notificationChannel)

        notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("asdfasdf")
            .setContentText("다운로드입니다.")
            .setSilent(true)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun downloadFile(uri: String) {
        val url = URL(uri)
        val httpCon = url.openConnection() as HttpURLConnection

        val fileSize = httpCon.contentLength
        val data = ByteArray(fileSize) { 0 }
        val inputStream = BufferedInputStream(httpCon.inputStream)
        // 파일 위치 cache/"companyCd"/fileType, 파일 이름 미정
        val outputFile = File(outputDirectory, uri.getFileName("tmp$fileExtension"))
        var count = inputStream.read(data)


        // 같은 이름 파일 삭제
        if (outputFile.exists()) {
            outputFile.delete()
        }

        val outputStream = FileOutputStream(outputFile)

        var total: Long = 0
        var downloadComplete = false

        // 알림 프로그래스바 업데이트
        while (count != -1) {
            total += count
            val progress = ((total * 100).toDouble() / fileSize.toDouble()).toInt()

            updateNotification(progress)
            outputStream.write(data, 0, count)
            downloadComplete = true
            count = inputStream.read(data)
        }
        onDownLoadComplete(downloadComplete)
        outputStream.apply {
            flush()
            close()
        }
        inputStream.close()
    }

    private fun updateNotification(currentProgress: Int) {
        notificationBuilder.let {
            it.apply {
                setProgress(100, currentProgress, false)
                setContentText("$currentProgress%")
            }
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun String.getFileName(defName:String = ""): String {
        var fileName:String? = null
        try {
            val uri = Uri.parse(this)
            fileName = uri.lastPathSegment
        } catch (e: Exception){

        }
        return fileName ?: defName
    }

    private fun onDownLoadComplete(downloadComplete: Boolean) {
        val message: String = if (downloadComplete) {
            "다운완료"
        } else {
            "다운실패"
        }
        Log.e("TAG", "onDownLoadComplete: $fileType")
        onDownloadProgressBar?.onSuccess()

        if (fileType == FileType.APK.toString()) {
            sendProgressUpdate(downloadComplete)
//            notificationClick(message)
            val intentFun = DownloadAndInstallApk.getInstance().getIntent(this)
            startActivity(intentFun)
        }
    }

    private fun notificationClick(message: String) {
        val intentFun = DownloadAndInstallApk.getInstance().getIntent(this)

        val notificationPendingIntent = PendingIntent.getActivity(
            this,
            PANDING_REQUEST_CODE,
            intentFun,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        notificationManager.cancel(NOTIFICATION_ID)
        notificationBuilder.apply {
            setProgress(0, 0, false)
            setContentTitle("다운로드 완료")
            setContentText(message)
            setContentIntent(notificationPendingIntent)
            setSmallIcon(android.R.drawable.stat_sys_download_done)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun sendProgressUpdate(downloadComplete: Boolean) {
        val intent = Intent(Const.APK_INTENT_ACTION)
        intent.putExtra(Const.APK_DOWNLOAD_COMPLETE, downloadComplete)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onStopCurrentWork(): Boolean {
        notificationManager.cancel(NOTIFICATION_ID)
        return super.onStopCurrentWork()
    }

    private fun getOutputDirectory(): File {
        return fileDir(fileType)
    }
}

interface OnDownloadProgressBar {
    fun onSuccess() {}
}
