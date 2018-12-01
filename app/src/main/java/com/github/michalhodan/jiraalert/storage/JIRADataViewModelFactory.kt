package com.github.michalhodan.jiraalert.storage

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.michalhodan.jira.sdk.Credentials
import com.github.michalhodan.jira.sdk.JIRA

class JIRADataViewModelFactory(private val url: String, private val authToken: String): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        JIRADataViewModel(JIRA.rest(Credentials.HttpAuth(url, authToken))) as T

}