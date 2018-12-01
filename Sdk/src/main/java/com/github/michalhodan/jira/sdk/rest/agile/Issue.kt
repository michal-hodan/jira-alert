package com.github.michalhodan.jira.sdk.rest.agile

import com.github.michalhodan.jira.sdk.rest.Rest
import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.parser.Parser

class Issue(client: Client, parser: Parser, board: Board.Response, sprint: Sprint.Response) : Rest.Agile(client, parser) {

    override val endpoint = "board/${board.id}/sprint/${sprint.id}/issue"

    suspend fun all() = client.get().deserialize<List>()

    data class List(val issues: kotlin.collections.List<Response>)

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
