package com.github.michalhodan.jiraalert.storage

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.github.michalhodan.jira.sdk.JIRA
import com.github.michalhodan.jiraalert.database.*
import com.github.michalhodan.jiraalert.database.User as UserEntity
import com.github.michalhodan.jiraalert.database.Issue as IssueEntity
import com.github.michalhodan.jiraalert.database.Board as BoardEntity
import com.github.michalhodan.jiraalert.database.Sprint as SprintEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class JIRADataViewModel(private val jira: JIRA, private val database: Database): ViewModel() {

    private val usersData: HashMap<String, MutableLiveData<UserEntity>> = hashMapOf()

    private lateinit var boardsData: MutableLiveData<Map<Int, BoardEntity>>

    private val sprintsData: HashMap<Int, MutableLiveData<Map<Int, SprintEntity>>> = hashMapOf()

    fun wipe() {
        usersData.clear()
        sprintsData.clear()
    }

    fun user(name: String) = usersData.getOrElse(name) {
        val userData = MutableLiveData<UserEntity>()

        GlobalScope.launch(Dispatchers.IO) {
            val data = with(database.user()) {
                retrieve(name) ?: jira.myself.get().run {
                    UserEntity(this.name, emailAddress, displayName, avatarUrls.`48x48`).also { insert(it) }
                }
            }
            userData.postValue(data)
        }

        usersData[name] = userData

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

    fun sprints(board: BoardEntity): MutableLiveData<Map<Int, SprintEntity>> = sprintsData.getOrElse(board.id) {
        val sprintData = MutableLiveData<Map<Int, SprintEntity>>()
        GlobalScope.launch(Dispatchers.IO) {
            val data = with(database.sprint()) {
                boardSprints().takeIf {
                    it.isNotEmpty()
                } ?: jira.sprint.all(board.id).values.map {
                    SprintEntity(it.id, board.id, it.name, it.state)
                }.also { insertAll(it) }
            }
            sprintData.postValue(data.associate {
                Pair(it.id, it)
            })
        }
        sprintData
    }

    fun activeSprint(board: BoardEntity): MutableLiveData<SprintEntity> {
        val sprintData =  MutableLiveData<SprintEntity>()
        GlobalScope.launch(Dispatchers.IO) {
            val data = with(database.sprint()) {
                boardSprints().takeIf {
                    it.isNotEmpty()
                } ?: jira.sprint.all(board.id).values.map {
                    SprintEntity(it.id, board.id, it.name, it.state)
                }.also { insertAll(it) }
            }
            sprintData.postValue(data.find { it.state == "active" })
        }
        return sprintData
    }

    fun issues(board: BoardEntity, sprint: SprintEntity): MutableLiveData<Map<Int, IssueEntity>> {
        val issueData = MutableLiveData<Map<Int, IssueEntity>>()
        GlobalScope.launch(Dispatchers.IO) {
            val data = with(database.issue()) {
                boardSprintIssues(board.id, sprint.id).takeIf {
                    it.isNotEmpty()
                } ?: jira.agileIssue.all(board.id, sprint.id).issues.map {
                    val storyPoints = it.fields.customfield_10006?.toInt()

                    IssueEntity(it.id, board.id, sprint.id, it.key, it.fields.summary, it.fields.created,
                        it.fields.description, storyPoints, it.fields.assignee.name, it.fields.creator.name,
                        it.fields.issuetype.id, it.fields.project.id, it.fields.priority.id, it.fields.status.id
                    )
                }.also { insertAll(it) }
            }
            issueData.postValue(data.associate {
                Pair(it.id, it)
            })
        }
        return issueData
    }

    fun boardIssueDataOfActiveSprint(boardId: Int): MutableLiveData<Pair<Configuration, List<TileData>>> {
        val issueData = MutableLiveData<Pair<Configuration, List<TileData>>>()
        GlobalScope.launch(Dispatchers.IO) {
            val configuration = with(database.configuration()) {
                retrieveByBoard(boardId) ?: jira.configuration.get(boardId).let {
                    Configuration(it.id, boardId, ArrayList(it.columnConfig.columns.map {
                        Column(it.name, it.statuses.map { it.id }, it.min, it.max)
                    }))
                }
            }

            val activeSprint = with(database.sprint()) {
                boardSprints().takeIf {
                    it.isNotEmpty()
                } ?: jira.sprint.all(boardId).values.map {
                    SprintEntity(it.id, boardId, it.name, it.state)
                }.also { insertAll(it) }
            }.find { it.state == "active" } ?: return@launch

            val issues =  with(database.issue()) {
                boardSprintIssues(boardId, activeSprint.id).takeIf {
                    it.isNotEmpty()
                } ?: jira.agileIssue.all(boardId, activeSprint.id).issues.map {

                    database.issueType().insert(
                        IssueType(it.fields.issuetype.id, it.fields.issuetype.name, it.fields.issuetype.iconUrl)
                    )
                    database.project().insert(
                        Project(it.fields.project.id, it.fields.project.key, it.fields.project.name, it.fields.project.avatarUrls.`48x48`)
                    )
                    database.priority().insert(
                        Priority(it.fields.priority.id, it.fields.priority.name, it.fields.priority.iconUrl)
                    )
                    database.user().insert(
                        UserEntity(it.fields.assignee.name, it.fields.assignee.emailAddress, it.fields.assignee.displayName, it.fields.assignee.avatarUrls.`48x48`)
                    )
                    database.user().insert(
                        UserEntity(it.fields.creator.name, it.fields.creator.emailAddress, it.fields.creator.displayName, it.fields.creator.avatarUrls.`48x48`)
                    )

                    val storyPoints = it.fields.customfield_10006?.toInt()

                IssueEntity(
                    it.id, boardId, activeSprint.id, it.key, it.fields.summary, it.fields.created, it.fields.description,
                    storyPoints, it.fields.assignee.name, it.fields.creator.name, it.fields.issuetype.id,
                    it.fields.project.id, it.fields.priority.id, it.fields.status.id
                )

                }.also { insertAll(it) }
            }

            issueData.postValue(configuration to issues.map {
                TileData(
                    it,
                    database.project().retrieve(it.projectId)!!,
                    database.priority().retrieve(it.priorityId)!!,
                    database.issueType().retrieve(it.issueTypeId)!!,
                    database.user().retrieve(it.assignee)!!,
                    database.user().retrieve(it.creator)!!
                )
            })
        }

        return issueData
    }

}