package com.github.michalhodan.jiraalert.storage

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.michalhodan.jira.sdk.Credentials
import com.github.michalhodan.jira.sdk.JIRA
import com.github.michalhodan.jiraalert.database.Database

class JIRADataViewModelFactory: ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        JIRADataViewModel(JIRA.rest(Credentials.HttpAuth(url, authToken)), Database.instance) as T

    companion object {
        private lateinit var url: String
        private lateinit var authToken: String

        fun bootstrap(url: String, authToken: String) {
            this.url = url
            this.authToken = authToken
        }
    }
}