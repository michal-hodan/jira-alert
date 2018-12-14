package com.github.michalhodan.jiraalert.storage

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.michalhodan.jira.sdk.Credentials
import com.github.michalhodan.jira.sdk.JIRA
import com.github.michalhodan.jiraalert.database.Database

class JIRADataViewModelFactory: ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) = when(modelClass) {
        JIRADataViewModel::class.java ->
            JIRADataViewModel(JIRA.rest(Credentials.HttpAuth(url, authToken)), Database.instance) as T
        JIRAIssueDataViewModel::class.java ->
            JIRAIssueDataViewModel(JIRA.rest(Credentials.HttpAuth(url, authToken)), Database.instance) as T
        else -> throw Exception("Unknown model class $modelClass")
    }

    companion object {
        private lateinit var url: String
        private lateinit var authToken: String

        fun bootstrap(url: String, authToken: String) {
            this.url = url
            this.authToken = authToken
        }
    }
}