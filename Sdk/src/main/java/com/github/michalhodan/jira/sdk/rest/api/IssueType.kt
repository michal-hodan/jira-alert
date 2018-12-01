package com.github.michalhodan.jira.sdk.rest.api

import com.github.michalhodan.jira.sdk.rest.Rest
import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.parser.Parser

class IssueType(client: Client, parser: Parser): Rest.Api(client, parser) {

    override val endpoint = "issuetype"

    suspend fun get() = client.get().deserialize<kotlin.collections.List<Response>>()

    data class Response(
        val id: Int,
        val name: String,
        val description: String,
        val iconUrl: String
    )
}