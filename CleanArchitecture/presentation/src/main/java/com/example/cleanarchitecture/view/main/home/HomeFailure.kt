package com.example.cleanarchitecture.view.main.home

import com.example.cleanarchitecture.core.exception.Failure.*

class HomeFailure {
    class ListNotAvailable : FeatureFailure()
    class NonExistentMovie : FeatureFailure()
}