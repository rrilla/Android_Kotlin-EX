package com.example.samplecleanarchitecture.view.main.home

import com.example.samplecleanarchitecture.core.exception.Failure.*

class HomeFailure {
    class ListNotAvailable : FeatureFailure()
    class NonExistentMovie : FeatureFailure()
}