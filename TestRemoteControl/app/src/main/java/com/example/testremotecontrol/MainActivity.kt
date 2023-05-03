package com.example.testremotecontrol

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import com.example.testremotecontrol.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        findViewById<Button>(R.id.gogo).setOnClickListener {
            SampleFragmentDialog().apply {
            }.show(
                supportFragmentManager, "SampleDialog"
            )
        }
    }
}