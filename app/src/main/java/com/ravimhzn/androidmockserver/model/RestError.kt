package com.ravimhzn.androidmockserver.model

import java.io.Serializable

//Should be same as StarThrowableError
data class RestError(
    var errorCode: Int,
    var statusCode: String,
    var statusMessage: String,
    var statusMsg: String,
    var error: String,
    var error_description: String
): Serializable
