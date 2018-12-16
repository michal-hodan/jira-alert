package com.github.michalhodan.jira.sdk.rest.response

data class AvatarUrl(
    val `48x48`: String,
    val `32x32`: String,
    val `24x24`: String,
    val `16x16`: String
)

data class IssueType(
    val id: Int,
    val name: String,
    val iconUrl: String
)

data class Priority(
    val id: Int,
    val name: String,
    val iconUrl: String
)

data class Project(
    val id: Int,
    val key: String,
    val name: String,
    val avatarUrls: AvatarUrl
)

data class Assignee(
    val name: String,
    val emailAddress: String,
    val displayName: String,
    val avatarUrls: AvatarUrl
)

data class Status(
    val id: Int,
    val name: String,
    val iconUrl: String,
    val description: String
)

data class StatusCategory(
    val id: Int,
    val key: String,
    val name: String,
    val color: String
)

