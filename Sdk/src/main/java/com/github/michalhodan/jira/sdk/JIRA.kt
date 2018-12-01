package com.github.michalhodan.jira.sdk

import com.github.michalhodan.jira.sdk.http.ApiClient
import com.github.michalhodan.jira.sdk.http.Client
import com.github.michalhodan.jira.sdk.parser.JsonParser
import com.github.michalhodan.jira.sdk.parser.Parser
import com.github.michalhodan.jira.sdk.rest.api.Myself

class JIRA private constructor(client: Client, parser: Parser) {

    val myself by lazy {
        Myself(client, parser)
    }


    companion object {
        fun rest(credentials: Credentials) = JIRA(ApiClient.Basic(credentials), JsonParser.newInstance())
    }
}