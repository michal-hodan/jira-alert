package com.github.michalhodan.jira.sdk

import com.github.kittinunf.fuel.util.Base64


sealed class Credentials(val url: String, val authToken: String) {

    class HttpAuth constructor(url: String, authToken: String): Credentials(url, authToken) {

        constructor(url: String,  username: String, password: String): this(url, "$username:$password".toBase64())

        private companion object {
            fun String.toBase64(): String = Base64.encodeToString(this.toByteArray(),Base64.NO_WRAP)
        }
    }

    override fun toString(): String {
        return "Credentials(url='$url', authToken='$authToken')"
    }


}