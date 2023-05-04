package com.example.cleanarchitecture.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.example.cleanarchitecture.R
import com.example.cleanarchitecture.databinding.ActivityLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    abstract fun inflateViewBinding(inflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = inflateViewBinding(layoutInflater)

        ActivityLayoutBinding.inflate(layoutInflater).apply {
            contentLayout.addView(binding.root)
        }
        setContentView(R.layout.activity_layout)
        val dd = ActivityLayoutBinding.inflate(layoutInflater).root.findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(dd)
    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(R.id.contentLayout) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }
}