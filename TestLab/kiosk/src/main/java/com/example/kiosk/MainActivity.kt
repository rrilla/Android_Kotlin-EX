package com.example.kiosk

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val devicePolicyAdmin = ComponentName(this, KioskDeviceAdminReceiver::class.java)


        devicePolicyManager.removeActiveAdmin(devicePolicyAdmin)


        with (binding) {
            button1.setOnClickListener {
                startLockTask()
            }

            button2.setOnClickListener {
                stopLockTask()
            }

            button3.setOnClickListener {
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, devicePolicyAdmin)
                // Start the add device admin activity
                startActivityForResult(intent, 100)
            }

            button4.setOnClickListener {
                Log.e("HJH", "${devicePolicyManager.isAdminActive(devicePolicyAdmin)}")
            }

            button5.setOnClickListener {
                devicePolicyManager.lockNow()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 100) {

            Log.e("HJH", "$${ActivityResult.resultCodeToString(resultCode)} , ${data?.dataString}")

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}