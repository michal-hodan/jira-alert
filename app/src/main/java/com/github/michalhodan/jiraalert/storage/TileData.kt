package com.github.michalhodan.jiraalert.storage

import com.github.michalhodan.jiraalert.database.*

data class TileData(
    val issue: IssueEntity,
    val project: Project,
    val priority: Priority,
    val issueType: IssueType,
    val assignee: User
)