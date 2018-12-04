package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*

@Entity
data class Issue(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "board_id") var boardId: Int,
    @ColumnInfo(name = "sprint_id") var sprintId: Int,
    val key: String,
    @ColumnInfo(name = "issue_type_id") var issueTypeId: Int,
    @ColumnInfo(name = "project_type_id") var projectTypeId: Int,
    @ColumnInfo(name = "priority_id") var priorityId: Int
)

@Dao
interface IssueDao {

    @Query("SELECT * FROM Issue WHERE board_id = :boardId AND sprint_id = :sprintId")
    fun boardSprintIssues(boardId: Int, sprintId: Int): List<Issue>

    @Insert
    fun insertAll(issues: List<Issue>)

}