package com.github.michalhodan.jiraalert.storage

import android.arch.lifecycle.ViewModel
import com.github.michalhodan.jira.sdk.JIRA

class JIRADataViewModel(private val jira: JIRA): ViewModel() {

    suspend fun user() = jira.myself.get()


}