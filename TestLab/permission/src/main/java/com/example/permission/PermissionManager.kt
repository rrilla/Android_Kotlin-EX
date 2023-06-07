package com.example.permission

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class PermissionManager @Inject constructor(
    private val activity: AppCompatActivity
) {

    enum class PermissionMember(
        val androidPermission: String
    ) {
        POST_NOTIFICATIONS(android.Manifest.permission.POST_NOTIFICATIONS),
        WRITE_EXTERNAL_STORAGE(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
        CAMERA(android.Manifest.permission.CAMERA)
    }

    interface PermissionSuccessListener {
        fun onSuccess()
    }

    private lateinit var listener: PermissionSuccessListener
    private lateinit var permissionMember: PermissionMember
    private val launcher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            Log.e("HJH", "$it")
            listener.onSuccess()
        } else {
            Log.e("HJH", "$it")
            Toast.makeText(activity, getMessageToPermissionDenied(permissionMember), Toast.LENGTH_SHORT).show()
        }
    }

    fun checkRequestPermission(permissionMember: PermissionMember, actionSuccess: () -> Unit) {
        Log.e("HJH", "checkRequestPermission() 호출")

        //  실행 버전이 해당 권한 필요없으면 성공이벤트 바로실행
        if (!checkNeedPermission(permissionMember)) {
            actionSuccess.invoke()
            return
        }

        this.listener = object: PermissionSuccessListener {
            override fun onSuccess() {
                actionSuccess.invoke()
            }
        }
        this.permissionMember = permissionMember

        when {
            ContextCompat.checkSelfPermission(
                activity,
                permissionMember.androidPermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                actionSuccess.invoke()
            }
            shouldShowRequestPermissionRationale(activity, permissionMember.androidPermission) -> {
                showInContextUI(permissionMember, launcher)
            }
            else -> {
                launcher.launch(permissionMember.androidPermission)
            }
        }
    }

    private fun checkNeedPermission(permissionMember: PermissionMember): Boolean {
        return when (permissionMember) {
            PermissionMember.POST_NOTIFICATIONS -> {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            }
            PermissionMember.WRITE_EXTERNAL_STORAGE -> {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.R
            }
            PermissionMember.CAMERA -> true
        }
    }

    private fun getMessageToPermissionDenied(permissionMember: PermissionMember): String {
        return when (permissionMember) {
            PermissionMember.POST_NOTIFICATIONS -> "알림 권한 거부로 인해 해당 기능을 사용할 수 없습니다."
            PermissionMember.WRITE_EXTERNAL_STORAGE -> "쓰기 권한 거부로 인해 해당 기능을 사용할 수 없습니다."
            PermissionMember.CAMERA -> "카메라 권한 거부로 인해 해당 기능을 사용할 수 없습니다."
        }
    }

    // TODO: 거절 시 해당 권한 왜 필요한지 가이드.
    private fun showInContextUI(permissionMember: PermissionMember, launcher: ActivityResultLauncher<String>) {
        Log.e("HJH", "showInContextUI() - ${permissionMember.name}")
        val message = when (permissionMember) {
            PermissionMember.POST_NOTIFICATIONS -> "~~~하기위해서 [알림] 권한이 필요합니다. 해당 권한을 수락 해주세요."
            PermissionMember.WRITE_EXTERNAL_STORAGE -> "~~~하기위해서 [쓰기] 권한이 필요합니다. 해당 권한을 수락 해주세요."
            PermissionMember.CAMERA -> "~~~하기위해서 [카메라] 권한이 필요합니다. 해당 권한을 수락 해주세요."
        }

        val snackBar = Snackbar.make((activity as MainActivity).findViewById(R.id.layout), message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("확인") { launcher.launch(permissionMember.androidPermission) }
//        snackBar.setActionTextColor(ContextCompat.getColor(appContext, R.color.colorTextPrimary))
        snackBar.show()
    }
}