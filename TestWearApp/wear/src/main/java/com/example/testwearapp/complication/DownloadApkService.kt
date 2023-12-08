package com.example.testwearapp.complication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.JobIntentService
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
}

class DownloadApkService : JobIntentService() {

    private lateinit var outputDirectory: File
    private lateinit var fileType: String
    private lateinit var fileExtension: String

    companion object {
        private const val rootDir = "test"
        const val defName = "tmp"

        lateinit var apkUrl: String

        fun enqueueWork(context: Context, work: Intent) {
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
        apkUrl = intent.getStringExtra(Const.APK_EXTRA).toString()

        // 다운로드 폴더 만들기
        outputDirectory = fileDir(fileType)

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

    private fun downloadFile(uri: String) {
        val url = URL(uri)
        val httpCon = url.openConnection() as HttpURLConnection

        val fileSize = httpCon.contentLength
        val data = ByteArray(fileSize) { 0 }
        val inputStream = BufferedInputStream(httpCon.inputStream)
        // 파일 위치 cache/{rootDir}/fileType, 파일 이름 미정
//        val outputFile = File(outputDirectory, uri.getFileName("$defName$fileExtension"))
        val outputFile = File(outputDirectory, "$defName$fileExtension")
        var count = inputStream.read(data)


        // 같은 이름 파일 삭제
        if (outputFile.exists()) {
            outputFile.delete()
        }

        val outputStream = FileOutputStream(outputFile)

        var total: Long = 0
        var downloadComplete = false

        while (count != -1) {
            total += count
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
        Log.e("TAG", "onDownLoadComplete: $fileType \n complete : $downloadComplete")

        if (fileType == FileType.APK.toString()) {
            val intentFun = DownloadAndInstallApk.getInstance().getIntent(this)
            startActivity(intentFun)
        }
    }

    override fun onStopCurrentWork(): Boolean {
        return super.onStopCurrentWork()
    }
}