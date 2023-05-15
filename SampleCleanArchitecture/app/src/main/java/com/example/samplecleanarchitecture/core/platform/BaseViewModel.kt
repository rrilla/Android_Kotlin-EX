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

import androidx.lifecycle.ViewModel
import com.example.samplecleanarchitecture.core.exception.Failure
import kotlinx.coroutines.flow.*

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 */
abstract class BaseViewModel : ViewModel() {

    private val _failure: MutableStateFlow<Failure?> = MutableStateFlow(null)
    val failure: StateFlow<Failure?> = _failure.asStateFlow()

    protected fun handleFailure(failure: Failure) {
        _failure.update { failure }
    }
}