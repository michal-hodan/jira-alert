package com.github.michalhodan.jira.sdk.parser

import kotlin.reflect.KClass
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class JsonParser private constructor(private val mapper: ObjectMapper): Parser {

    override fun <T : Any> deserialize(data: String, `class`: KClass<T>): T = mapper.readValue(data, `class`.java)

    companion object {
        private val instance = jacksonObjectMapper()

        init {
            instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

        fun newInstance() = JsonParser(instance)
    }
}