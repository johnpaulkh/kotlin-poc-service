package org.johnpaulkh.poc.base

import com.fasterxml.jackson.databind.ObjectMapper
import org.johnpaulkh.poc.util.getContent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult

@SpringIntegrationTest
open class IntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objMapper: ObjectMapper

    final inline fun <reified T> MvcResult?.content() = contentOrNull<T>()!!

    final inline fun <reified T> MvcResult?.contentOrNull() = this?.let { getContent<T>(this) }

}