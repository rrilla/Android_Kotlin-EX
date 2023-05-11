package com.example.cleanarchitecture.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.cleanarchitecture.R
import com.example.cleanarchitecture.databinding.ActivityLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity() {

    abstract fun inflateViewBinding(inflater: LayoutInflater): VB

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = inflateViewBinding(layoutInflater)

        ActivityLayoutBinding.inflate(layoutInflater).apply {
            contentLayout.addView(binding.root)

            setContentView(root)
            setSupportActionBar(root.findViewById(R.id.toolbar))
        }
    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(R.id.contentLayout) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }
}