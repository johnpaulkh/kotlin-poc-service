package org.johnpaulkh.poc.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.test.web.servlet.MvcResult

val objectMapper: ObjectMapper = jacksonObjectMapper()
    .registerModules(JavaTimeModule())

inline fun <reified T> getContent(mvcResponse: MvcResult): T =
    objectMapper.readValue<T>(mvcResponse.response.contentAsString)