package com.github.michalhodan.jira.sdk.http

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.HttpException
import com.github.michalhodan.jira.sdk.Credentials

internal sealed class ApiClient(credentials: Credentials, headers: HashMap<String, String>): Client {

    init {
        FuelManager.instance.apply {
            basePath = credentials.url
            baseHeaders = headers + ("Accept" to "application/json")
        }
    }

    override suspend fun request(request: Request): Response {
        val result = Fuel.get(request.path).response()
        val error = result.third.component2()
        if (error != null) {
            when(error.response.statusCode) {
                401 -> throw com.github.michalhodan.jira.sdk.http.HttpException.Unauthorized(error.response.data.toString())
                403 -> throw com.github.michalhodan.jira.sdk.http.HttpException.Forbidden(error.response.data.toString())
                else -> TODO("Implement response for ${error.response.statusCode}")
            }
        }

        return result.run {
            object: Response {
                override val statusCode = Response.StatusCode.valueOf(second.statusCode)
                override val headers = second.headers
                override val body = String(third.component1()!!)
            }
        }
    }

    class Basic(credentials: Credentials):
        ApiClient(credentials, hashMapOf("Authorization" to "Basic ${credentials.authToken}"))
}