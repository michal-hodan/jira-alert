package com.github.michalhodan.jira.sdk.rest.agile

import com.github.michalhodan.jira.sdk.rest.Rest
import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.http.Request
import com.github.michalhodan.jira.sdk.parser.Parser

class Sprint(client: Client, parser: Parser): Rest.Agile(client, parser) {

    override val endpoint = "board"

    suspend fun all(boardId: Int) = client
        .request(Request.get("$path/$boardId/sprint"))
        .deserialize<List>()

    data class List(val values: kotlin.collections.List<Response>)

    data class Response(
        val id: Int,
        val state: String,
        val name: String,
        val goal: String,
        val startDate: String?,
        val endDate: String?,
        val completeDate: String?
    )
}