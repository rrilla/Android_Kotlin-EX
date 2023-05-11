package com.example.cleanarchitecture.view.main

import android.os.Bundle
import android.view.LayoutInflater
import com.example.cleanarchitecture.R
import com.example.cleanarchitecture.core.extenstion.inTransaction
import com.example.cleanarchitecture.core.platform.BaseActivity
import com.example.cleanarchitecture.databinding.ActivityMainBinding
import com.example.cleanarchitecture.view.main.home.HomeFragment
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