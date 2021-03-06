package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*

typealias IssueEntity = Issue

@Entity(foreignKeys = [
    ForeignKey(
        entity = Board::class,
        parentColumns = ["id"],
        childColumns = ["board_id"]
    ),
    ForeignKey(
        entity = Sprint::class,
        parentColumns = ["id"],
        childColumns = ["sprint_id"]
    ),
    ForeignKey(
        entity = User::class,
        parentColumns = ["name"],
        childColumns = ["assignee_name"]
    ),
    ForeignKey(
        entity = IssueType::class,
        parentColumns = ["id"],
        childColumns = ["issue_type_id"]
    ),
    ForeignKey(
        entity = Project::class,
        parentColumns = ["id"],
        childColumns = ["project_id"]
    ),
    ForeignKey(
        entity = Priority::class,
        parentColumns = ["id"],
        childColumns = ["priority_id"]
    )
])
data class Issue(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "board_id") var boardId: Int,
    @ColumnInfo(name = "sprint_id") var sprintId: Int,
    val key: String,
    val summary: String,
    val created: String,
    val description: String?,
    @ColumnInfo(name = "story_points") val storyPoints: Int?,
    @ColumnInfo(name = "assignee_name") val assignee: String,
    @ColumnInfo(name = "creator_name") val creator: String,
    @ColumnInfo(name = "issue_type_id") var issueTypeId: Int,
    @ColumnInfo(name = "project_id") var projectId: Int,
    @ColumnInfo(name = "priority_id") var priorityId: Int,
    @ColumnInfo(name = "status_id") var statusId: Int
)

@Dao
interface IssueDao {

    @Query("SELECT * FROM Issue WHERE id = :id LIMIT 1")
    fun findById(id: Int): Issue?

    @Query("SELECT * FROM Issue WHERE board_id = :boardId AND sprint_id = :sprintId")
    fun boardSprintIssues(boardId: Int, sprintId: Int): List<Issue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(issues: List<Issue>)

}