/**
 * Copyright (C) 2020 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.samplecleanarchitecture.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.samplecleanarchitecture.R
import com.example.samplecleanarchitecture.databinding.ActivityLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

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
