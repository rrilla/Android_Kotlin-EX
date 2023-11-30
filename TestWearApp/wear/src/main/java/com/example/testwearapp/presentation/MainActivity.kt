/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.testwearapp.presentation

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import coil.compose.AsyncImage
import com.example.testwearapp.R
import com.example.testwearapp.complication.App
import com.example.testwearapp.complication.Const.APK_EXTRA
import com.example.testwearapp.complication.Const.FILE_EXTENSION
import com.example.testwearapp.complication.Const.FILE_NOTIFICATION
import com.example.testwearapp.complication.Const.FILE_TYPE
import com.example.testwearapp.complication.DownloadApkService
import com.example.testwearapp.complication.DownloadApkService.Companion.apkUrl
import com.example.testwearapp.complication.FileType
import com.example.testwearapp.presentation.theme.TestWearAppTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val packageInfo = App.get().packageManager.getPackageInfo(App.get().packageName, 0)

            WearApp("current version :\n ${packageInfo.versionName}")

            val notificationChannel = NotificationChannel(
                getString(R.string.channel_id),
                "채널설명",
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(
                this
            ).createNotificationChannel(notificationChannel)


            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1994)

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("HJH", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                Log.d("HJH", token)
            })

        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    TestWearAppTheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            Greeting(greetingName = greetingName)
            Button(onClick = {
                val serviceIntent = Intent(App.get(), DownloadApkService::class.java)
                // apk 다운로드
//                serviceIntent.putExtra(APK_EXTRA, "https://drive.google.com/u/0/uc?id=1HpwCvxItLxQ6p280X9xiEMTuZtV84syF&export=download&confirm=t&uuid=3d0bd739-7f4e-426e-a866-540f3ad07b9b&at=AB6BwCDIgfUJMjliWEEDuio3vZLL:1701239371899")
                serviceIntent.putExtra(APK_EXTRA, "https://drive.google.com/u/0/uc?id=1HpwCvxItLxQ6p280X9xiEMTuZtV84syF&export=download&confirm=t&uuid=d249b990-512e-4b46-99bf-290cf76d0d5a&at=AB6BwCCzfIvHwbSq-01dZsPGp7uU:1701305780172")
                serviceIntent.putExtra(FILE_TYPE, FileType.APK.toString())
                serviceIntent.putExtra(FILE_EXTENSION, FileType.APK.extension)
                serviceIntent.putExtra(FILE_NOTIFICATION, true)

                DownloadApkService.enqueueWork(App.get(), serviceIntent)
            }) {}
//            AsyncImage(
//                model = "http://192.168.0.53:9405/public/baby12.jpg",
//                contentDescription = "Translated description of what the image contains"
//            )
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName),
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}