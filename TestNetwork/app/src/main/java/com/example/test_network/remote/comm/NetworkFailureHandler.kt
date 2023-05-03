package com.example.test_network.remote.comm

import com.example.test_network.remote.comm.VCExceptionCode.*

object NetworkFailureHandler {
    const val TAG = "NetworkFailureHandler"

    fun makeException(code: VCExceptionCode = NONE, httpCode: Int = -1, errorMessage: String?) : VCResponseException {
        val exceptionCode: VCExceptionCode = when (code) {
            HTTP_FAILED -> HTTP_FAILED
            BODY_IS_NULL -> BODY_IS_NULL
            STATUS_NOT_OK -> STATUS_NOT_OK
            else -> NONE
        }

        return VCResponseException(exceptionCode, "httpCode: $httpCode, message: $errorMessage")
    }

    fun checkVCResponseException(e: Exception): VCResponseException {
        return if (e is VCResponseException) {
            VCResponseException(e.code, e.message)
        } else {
            VCResponseException(NONE, e.message)
        }
    }

}