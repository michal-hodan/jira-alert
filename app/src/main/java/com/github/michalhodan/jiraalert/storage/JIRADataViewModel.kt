package com.github.michalhodan.jiraalert.storage

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.michalhodan.jira.sdk.JIRA
import com.github.michalhodan.jiraalert.database.Database
import com.github.michalhodan.jiraalert.database.User as UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class JIRADataViewModel(private val jira: JIRA, private val database: Database): ViewModel() {

    private lateinit var userData: MutableLiveData<UserEntity>

    fun user(): MutableLiveData<UserEntity> {
        if (!::userData.isInitialized) {
            userData = MutableLiveData()
            GlobalScope.launch(Dispatchers.IO) {
                val data = with(database.user()) {
                    retrieve() ?: jira.myself.get().run {
                        UserEntity(emailAddress, displayName, avatarUrls.`48x48`).also { insert(it) }
                   }
                }
                userData.postValue(data)
            }
        }
        return userData
    }

}