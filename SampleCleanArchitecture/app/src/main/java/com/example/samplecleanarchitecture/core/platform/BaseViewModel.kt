package com.example.samplecleanarchitecture.core.platform

import androidx.lifecycle.ViewModel
import com.example.samplecleanarchitecture.core.exception.Failure
import kotlinx.coroutines.flow.*

abstract class BaseViewModel : ViewModel() {

    private val _failure: MutableStateFlow<Failure?> = MutableStateFlow(null)
    val failure: StateFlow<Failure?> = _failure.asStateFlow()

    protected fun handleFailure(failure: Failure) {
        _failure.update { failure }
    }

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    protected fun handleLoading(loading: Boolean) {
        _loading.update { loading }
    }
}