package com.example.test_network.remote.comm

class VCMsgException(val msg: String, val code: VCExceptionCode = VCExceptionCode.NONE) : Exception(msg) {

}

enum class VCExceptionCode {
    NONE,
    FIRMWARE_RECONNECT_TIMEOUT,
    LAYOUT_UPDATE_ERROR,

    HTTP_FAILED,
    BODY_IS_NULL,
    STATUS_NOT_OK,
    HTTP_401
}