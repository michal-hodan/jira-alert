package com.github.michalhodan.jiraalert.storage

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.michalhodan.jira.sdk.JIRA
import com.github.michalhodan.jiraalert.database.Database
import com.github.michalhodan.jiraalert.database.User as UserEntity
import com.github.michalhodan.jiraalert.database.Issue as IssueEntity
import com.github.michalhodan.jiraalert.database.Board as BoardEntity
import com.github.michalhodan.jiraalert.database.Sprint as SprintEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class JIRADataViewModel(private val jira: JIRA, private val database: Database): ViewModel() {

    private lateinit var userData: MutableLiveData<UserEntity>

    private lateinit var boardsData: MutableLiveData<Map<Int, BoardEntity>>

    private val sprintsData: Map<Int, MutableLiveData<Map<Int, SprintEntity>>> = mapOf()

    fun user(): MutableLiveData<UserEntity> {
        if (::userData.isInitialized) {
            return userData
        }
        userData = MutableLiveData()

        GlobalScope.launch(Dispatchers.IO) {
            val data = with(database.user()) {
                retrieve() ?: jira.myself.get().run {
                    UserEntity(emailAddress, displayName, avatarUrls.`48x48`).also { insert(it) }
               }
            }
            userData.postValue(data)
        }

        return userData
    }

    fun boards(): MutableLiveData<Map<Int, BoardEntity>> {
        if (::boardsData.isInitialized) {
            return boardsData
        }
        boardsData = MutableLiveData()

        GlobalScope.launch(Dispatchers.IO) {
            val data = with(database.board()) {
                retrieveAll().takeIf {
                    it.isNotEmpty()
                } ?: jira.boards.all().values.map {
                    BoardEntity(it.id, it.name, it.type)
                }.also { insertAll(it) }
            }
            boardsData.postValue(data.associate {
                Pair(it.id, it)
            })
        }

        return boardsData
    }

    fun sprints(boardId: Int): MutableLiveData<Map<Int, SprintEntity>> = sprintsData.getOrElse(boardId) {
        val sprintData = MutableLiveData<Map<Int, SprintEntity>>()
        GlobalScope.launch(Dispatchers.IO) {
            val data = with(database.sprint()) {
                boardSprints(boardId).takeIf {
                    it.isNotEmpty()
                } ?: jira.sprint.all(boardId).values.map {
                    SprintEntity(it.id, boardId, it.name, it.state)
                }.also { insertAll(it) }
            }
            sprintData.postValue(data.associate {
                Pair(it.id, it)
            })
        }
        sprintData
    }

    fun issues(boardId: Int, sprintId: Int): MutableLiveData<Map<Int, IssueEntity>> {
        val issueData = MutableLiveData<Map<Int, IssueEntity>>()
        GlobalScope.launch(Dispatchers.IO) {
            val data = with(database.issue()) {
                boardSprintIssues(boardId, sprintId).takeIf {
                    it.isNotEmpty()
                } ?: jira.issue.all(boardId, sprintId).issues.map {
                    IssueEntity(it.id, boardId, sprintId, it.key, it.fields.issuetype.id, it.fields.project.id, it.fields.priority.id)
                }.also { insertAll(it) }
            }
            issueData.postValue(data.associate {
                Pair(it.id, it)
            })
        }
        return issueData
    }

}