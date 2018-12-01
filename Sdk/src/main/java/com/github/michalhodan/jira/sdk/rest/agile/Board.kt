package com.github.michalhodan.jira.sdk.rest.agile

import com.github.michalhodan.jira.sdk.rest.Rest
import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.parser.Parser

class Board(client: Client, parser: Parser): Rest.Agile(client, parser) {

    override val endpoint = "board"

    suspend fun get(id: Int) = client.get(id).deserialize<Response>()

    suspend fun all() = client.get().deserialize<List>()

    data class List(val values: kotlin.collections.List<Response>)

    data class Response(
        val id: Int,
        val name: String,
        val type: String
    )
}