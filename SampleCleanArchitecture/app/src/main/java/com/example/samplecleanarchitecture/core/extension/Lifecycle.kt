package com.example.samplecleanarchitecture.core.extension

import android.app.Activity
import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.samplecleanarchitecture.R
import com.example.samplecleanarchitecture.core.exception.Failure
import com.example.samplecleanarchitecture.core.platform.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

//fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
//    liveData.observe(this, Observer(body))
//
//fun <L : LiveData<Failure>> LifecycleOwner.failure(liveData: L, body: (Failure?) -> Unit) =
//    liveData.observe(this, Observer(body))

fun <T : Any, L : StateFlow<T>> LifecycleOwner.observe(flow: L, body: (T?) -> Unit) =
    this.lifecycleScope.launchWhenStarted { flow.collect(body) }

fun <L : StateFlow<Failure?>> LifecycleOwner.failure(flow: L, body: (Failure?) -> Unit) =
    this.lifecycleScope.launchWhenStarted { flow.collect(body) }

fun <L : StateFlow<Boolean>> LifecycleOwner.loading(flow: L, activity: Activity) =
    this.lifecycleScope.launchWhenStarted { flow.collect{
        if (activity is BaseActivity<*>) {
            activity.findViewById<FrameLayout>(R.id.progress).isGone = !it
        }
    } }