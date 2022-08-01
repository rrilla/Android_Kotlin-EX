package com.example.test_network.remote.comm

import java.io.IOException

/**
 * API Call 공통 Exception
 *
 * @param code : 에러 분류하기 위한 코드 / DEFAULT = NONE
 * @param message : status 에러 메세지 + error message
 */
open class VCResponseException(
    val code: VCExceptionCode = VCExceptionCode.NONE,
    override val message: String? = null
): IOException(message) {

}