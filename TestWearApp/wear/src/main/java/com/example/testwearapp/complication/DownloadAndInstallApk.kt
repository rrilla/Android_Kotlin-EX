package com.example.testwearapp.complication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.example.testwearapp.complication.DownloadApkService.Companion.apkUrl
import java.io.File


class DownloadAndInstallApk {
    companion object {
        private var instance: DownloadAndInstallApk? = null

        @JvmStatic
        fun getInstance(): DownloadAndInstallApk =
            instance ?: synchronized(this) {
                instance ?: DownloadAndInstallApk().also { instance = it }
            }
    }

    fun String.getFileName(defName: String = ""): String {
        var fileName:String? = null
        try {
            val uri = Uri.parse(this)
            fileName = uri.lastPathSegment
        } catch (e: Exception){

        }
        return fileName ?: defName
    }

    fun getIntent(context: Context): Intent {
        val file =
            File(
                DownloadApkService.fileDir(FileType.APK.toString()).apply {
                    mkdirs()
                },
//                apkUrl.getFileName("tmp.apk").also { Log.e("HJH", "fileName = $it") }
                "${DownloadApkService.defName}${FileType.APK.extension}"
            )
        Log.e("HJH", "filePath :  ${file.path}")

        val apkUri =  FileProvider.getUriForFile(
            context,
            "com.example.testwearapp" + ".fileprovider",
            file
        )

        val openFileIntent = Intent(Intent.ACTION_VIEW).apply {
            putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
//            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(apkUri, "application/vnd.android.package-archive")
        }
        return openFileIntent
    }
}