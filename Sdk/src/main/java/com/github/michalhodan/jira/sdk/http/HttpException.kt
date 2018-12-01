package com.github.michalhodan.jira.sdk.http

import java.lang.Exception

sealed class HttpException(val status: Response.StatusCode, message: String, previous: Exception? = null): Exception() {

    class Unauthorized(message: String): HttpException(Response.StatusCode.UNAUTHORIZED, message)

    class Forbidden(message: String): HttpException(Response.StatusCode.FORBIDDEN, message)

}