package com.example.log

import android.app.Application
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.CsvFormatStrategy
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.DiskLogStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.lang.NumberFormatException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.io.path.name

class App: Application() {


    override fun onCreate() {
        super.onCreate()

        //  Logger Tag
        val TAG = "Logger"
        //  로그 파일 확장자
        val EXTENSION = ".txt"
        //  로그 파일 저장 수
        val SAVE_COUNT = 3
        //  로그 파일 저장 경로
        val BASE_PATH = "/storage/emulated/0/Android/data/kr.co.voicecaddie.t5droid/cache"
        //  로그 파일 저장 폴더명
        val FOLDER_NAME = "Logs"

        val dir = File(externalCacheDir?.path ?: BASE_PATH, FOLDER_NAME).also { Log.e(TAG, "Log File Path : ${it.path}") }
        if (!dir.exists()) dir.mkdir()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val array = arrayListOf<Int>()
            Files.walk(Paths.get(dir.path)).forEach { path ->
                Log.e(TAG, "Existing FileName : ${path.name}")
                try {
                    array.add(path.name.replace(EXTENSION, "").toInt())
                } catch (e: NumberFormatException) {}
            }

            if (array.count() > SAVE_COUNT) {
                try {
                    array.sort()
                    val deleteArray = array.slice(0..array.size-1-SAVE_COUNT)
                    deleteArray.forEach {
                        Log.e(TAG, "Delete FileName = $it$EXTENSION")
                        File(dir, "$it$EXTENSION").delete()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        val startDate = SimpleDateFormat("yyyyMMdd").format(Date()).toInt().also { Log.e(TAG, "startDate - $it") }
        val file = File(dir, "$startDate$EXTENSION")
        val handler = Handler {
            var fileWriter: FileWriter? = null
            try {
                val currentDate = SimpleDateFormat("yyyyMMdd").format(Date()).toInt()
                val currentTime = SimpleDateFormat("HH:mm:ss").format(Date())
                val file2 = if (currentDate > startDate) {
                    File(dir, "$currentDate$EXTENSION")
                } else {
                    file
                }
                val level = when (it.what) {
                    Logger.VERBOSE -> {
                        Log.v(TAG, "${it.obj}")
                        "V"
                    }
                    Logger.DEBUG -> {
                        Log.d(TAG, "${it.obj}")
                        "D"
                    }
                    Logger.INFO -> {
                        Log.i(TAG, "${it.obj}")
                        "I"
                    }
                    Logger.WARN -> {
                        Log.w(TAG, "${it.obj}")
                        "W"
                    }
                    Logger.ERROR -> {
                        Log.e(TAG, "${it.obj}")
                        "E"
                    }
                    else -> {
                        Log.e(TAG, "${it.obj}")
                        "N"
                    }
                }

                // TODO: 파일 사이즈 제한 필요?
                fileWriter = FileWriter(file2, true)
                fileWriter.appendLine("$currentTime   $level  ${it.obj}")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fileWriter?.let { writer ->  
                    writer.flush()
                    writer.close()
                }
            }
            true
        }

        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(1)
            .logStrategy(DiskLogStrategy(handler))
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }
}