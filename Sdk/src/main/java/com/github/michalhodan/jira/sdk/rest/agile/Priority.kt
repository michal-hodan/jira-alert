package com.github.michalhodan.jira.sdk.rest.agile

class Priority {

    data class Response(
        val id: Int,
        val name: String,
        val iconUrl: String
    )
}