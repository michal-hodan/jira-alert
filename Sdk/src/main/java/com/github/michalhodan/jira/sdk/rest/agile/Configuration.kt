package com.github.michalhodan.jira.sdk.rest.agile

import com.github.michalhodan.jira.sdk.rest.Rest
import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.http.Request
import com.github.michalhodan.jira.sdk.parser.Parser

class Configuration(client: Client, parser: Parser) : Rest.Agile(client, parser) {

    override val endpoint = "board"

    suspend fun get(boardId: Int) = client
        .request(Request.get("$path/$boardId/configuration"))
        .deserialize<Response>()

    data class Response(
        val id: Int,
        val name: String,
        val columnConfig: ColumnConfig
    ) {
        data class ColumnConfig(
            val columns: List<Column>
        ) {
            data class Column(
                val name: String,
                val statuses: List<Status>,
                val min: Int?,
                val max: Int?
            ) {
                data class Status(
                    val id: Int
                )
            }
        }
    }
}