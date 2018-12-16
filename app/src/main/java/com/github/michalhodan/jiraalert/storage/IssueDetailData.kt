package com.github.michalhodan.jiraalert.storage

import com.github.michalhodan.jiraalert.database.*

data class IssueDetailData(
    val issue: IssueEntity,
    val sprint: Sprint,
    val project: Project,
    val priority: Priority,
    val issueType: IssueType,
    val assignee: User,
    val creator: User
)