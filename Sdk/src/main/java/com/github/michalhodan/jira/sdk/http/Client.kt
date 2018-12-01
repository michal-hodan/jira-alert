package com.github.michalhodan.jira.sdk.http

interface Client {
    @Throws(HttpException::class)
    suspend fun request(request: Request): Response
}