package com.github.michalhodan.jiraalert.storage

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.michalhodan.jira.sdk.JIRA
import com.github.michalhodan.jiraalert.database.Database
import com.github.michalhodan.jiraalert.database.User as UserEntity
import com.github.michalhodan.jiraalert.database.Board as BoardEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class JIRADataViewModel(private val jira: JIRA, private val database: Database): ViewModel() {

    private lateinit var userData: MutableLiveData<UserEntity>

    private lateinit var boardsData: MutableLiveData<Map<Int, BoardEntity>>

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
            val data = with(database.boards()) {
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



}