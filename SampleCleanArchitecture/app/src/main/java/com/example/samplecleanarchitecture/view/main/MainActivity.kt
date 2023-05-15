package com.example.samplecleanarchitecture.view.main

import android.os.Bundle
import android.view.LayoutInflater
import com.example.samplecleanarchitecture.R
import com.example.samplecleanarchitecture.core.extension.inTransaction
import com.example.samplecleanarchitecture.core.platform.BaseActivity
import com.example.samplecleanarchitecture.databinding.ActivityMainBinding
import com.example.samplecleanarchitecture.view.main.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun inflateViewBinding(inflater: LayoutInflater) = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.inTransaction {
            add(
                R.id.fragmentContainer,
                HomeFragment()
            )
        }
    }
}