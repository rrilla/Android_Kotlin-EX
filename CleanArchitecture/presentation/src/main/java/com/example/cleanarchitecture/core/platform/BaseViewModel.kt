package com.example.cleanarchitecture.core.platform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cleanarchitecture.core.exception.Failure

abstract class BaseViewModel : ViewModel() {

    private val _failure: MutableLiveData<Failure> = MutableLiveData()
    val failure: LiveData<Failure> = _failure

    protected fun handleFailure(failure: Failure) {
        _failure.value = failure
    }
}