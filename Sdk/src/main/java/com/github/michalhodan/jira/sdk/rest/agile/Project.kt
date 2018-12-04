package com.github.michalhodan.jira.sdk.rest.agile

class Project {

    data class Response(
        val id: Int,
        val key: String,
        val name: String
    ) {
        data class AvatarUrls(
            val `48x48`: String
        )
    }
}