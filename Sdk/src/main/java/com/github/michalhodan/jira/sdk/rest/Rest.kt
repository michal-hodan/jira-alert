package com.github.michalhodan.jira.sdk.rest

import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.http.Request
import com.github.michalhodan.jira.sdk.http.Response
import com.github.michalhodan.jira.sdk.parser.Parser

sealed class Rest(protected val client: Client, protected val parser: Parser) {

    protected abstract val endpoint: String

    protected abstract val type: Type

    protected val path
        get() = "rest/${type.type}/${type.version}/$endpoint"

    protected enum class Type(val type: String, val version: String) {
        AGILE_1("agile", "1.0"), API_2("api", "2")
    }

    abstract class Agile(client: Client, parser: Parser): Rest(client, parser) {
        override val type = Type.AGILE_1
    }

    abstract class Api(client: Client, parser: Parser): Rest(client, parser) {
        override val type = Type.API_2
    }

    protected suspend fun Client.get() = request(Request.get(path))

    protected suspend fun Client.post() = request(Request.post(path))

    protected inline fun <reified T : Any> Response.deserialize() = parser.deserialize(body, T::class)
}
