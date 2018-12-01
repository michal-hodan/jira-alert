package com.github.michalhodan.jira.sdk.parser

import kotlin.reflect.KClass

interface Parser {
    fun <T: Any>deserialize(data: String, `class`: KClass<T>): T
}