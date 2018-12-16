package com.github.michalhodan.jira.sdk.rest.api

import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.http.Request
import com.github.michalhodan.jira.sdk.parser.Parser
import com.github.michalhodan.jira.sdk.rest.Rest
import com.github.michalhodan.jira.sdk.rest.response.*
import com.github.michalhodan.jira.sdk.rest.response.IssueType

class Issue(client: Client, parser: Parser): Rest.Api(client, parser) {

    override val endpoint = "issue"

    suspend fun detail(id: Int) = client
        .request(Request.get("$path/$id"))
        .deserialize<Response>()

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