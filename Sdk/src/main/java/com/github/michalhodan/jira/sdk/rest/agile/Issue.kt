package com.github.michalhodan.jira.sdk.rest.agile

import com.github.michalhodan.jira.sdk.rest.Rest
import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.http.Request
import com.github.michalhodan.jira.sdk.parser.Parser
import com.github.michalhodan.jira.sdk.rest.response.*

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
            val issuetype: IssueType,
            val project: Project,
            val priority: Priority,
            val customfield_10006: Float?,
            val summary : String,
            val description: String?,
            val created: String,
            val assignee: Assignee,
            val creator: Assignee,
            val status: Status
        )
    }
}
