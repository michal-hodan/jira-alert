package com.github.michalhodan.jira.sdk.rest.agile

import com.github.michalhodan.jira.sdk.rest.Rest
import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.http.Request
import com.github.michalhodan.jira.sdk.parser.Parser

class Issue(client: Client, parser: Parser) : Rest.Agile(client, parser) {

    override val endpoint = "board"

    suspend fun all(boardIt: Int, sprintId: Int) = client
        .request(Request.get("$path/$boardIt/sprint/$sprintId/issue"))
        .deserialize<List>()

    data class List(val issues: kotlin.collections.List<Response>)

    data class Response(
        val id: Int,
        val key: String,
        val fields: Issue
    ) {
        data class Issue(
            val issuetype: IssueType.Response,
            val project: Project.Response,
            val priority: Priority.Response,
            val summary : String,
            val assignee: Assignee
        ) {
            data class Assignee(
                val name: String
            )
        }
    }
}
