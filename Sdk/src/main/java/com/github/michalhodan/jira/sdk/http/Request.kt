package com.github.michalhodan.jira.sdk.http

sealed class Request(val path: String) {

    abstract val method: Method

    class Get(path: String): Request(path) {
        override val method = Method.GET
    }

    class Post(path: String): Request(path) {
        override val method = Method.POST
    }

    companion object Factory {
        fun get(endpoint: String) = Request.Get(endpoint)
        fun post(endpoint: String) = Request.Post(endpoint)
    }

    enum class Method {
        GET, POST
    }
}