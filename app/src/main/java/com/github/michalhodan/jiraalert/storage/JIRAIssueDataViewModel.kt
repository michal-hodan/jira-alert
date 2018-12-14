package com.github.michalhodan.jiraalert.storage

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.michalhodan.jira.sdk.JIRA
import com.github.michalhodan.jiraalert.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import com.github.michalhodan.jiraalert.database.User as UserEntity
import com.github.michalhodan.jiraalert.database.Issue as IssueEntity
import com.github.michalhodan.jiraalert.database.Board as BoardEntity
import com.github.michalhodan.jiraalert.database.Sprint as SprintEntity

class JIRAIssueDataViewModel(private val jira: JIRA, private val database: Database): ViewModel() {

    private val issue: HashMap<Int, MutableLiveData<TileData>> = hashMapOf()

    fun detail(issueId: Int) = issue.getOrPut(issueId) {
        MutableLiveData<TileData>().apply {
            GlobalScope.launch(Dispatchers.IO) {
                postValue(load(issueId))
            }
        }
    }

    private fun load(issueId: Int): TileData {
        val issue = database.issue().findById(issueId)!!
        return TileData(
            issue = issue,
            creator = database.user().retrieve(issue.creator)!!,
            assignee = database.user().retrieve(issue.assignee)!!,
            project = database.project().retrieve(issue.projectId)!!,
            priority = database.priority().retrieve(issue.priorityId)!!,
            issueType = database.issueType().retrieve(issue.issueTypeId)!!
        )
    }
}