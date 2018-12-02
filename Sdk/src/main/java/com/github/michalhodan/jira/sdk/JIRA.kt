package com.github.michalhodan.jira.sdk

import com.github.michalhodan.jira.sdk.http.ApiClient
import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.parser.JsonParser
import com.github.michalhodan.jira.sdk.parser.Parser
import com.github.michalhodan.jira.sdk.rest.agile.Board
import com.github.michalhodan.jira.sdk.rest.api.Myself

class JIRA private constructor(private val client: Client, private val parser: Parser) {

    val myself get() = Myself(client, parser)

    val boards get() = Board(client, parser)


    companion object {
        fun rest(credentials: Credentials) = JIRA(ApiClient.Basic(credentials), JsonParser.newInstance())
    }
}