package com.github.michalhodan.jira.sdk.rest.api

import com.github.michalhodan.jira.sdk.rest.Rest
import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.parser.Parser
import com.github.michalhodan.jira.sdk.rest.response.AvatarUrl

class Myself(client: Client, parser: Parser): Rest.Api(client, parser) {

    override val endpoint = "myself"

    suspend fun get() = client.get().deserialize<Response>()

    data class Response(
        val name: String,
        val displayName: String,
        val emailAddress: String,
        val locale: String,
        val timeZone: String,
        val avatarUrls: AvatarUrl
    )
}