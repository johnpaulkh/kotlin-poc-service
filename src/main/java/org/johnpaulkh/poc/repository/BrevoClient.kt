package org.johnpaulkh.poc.repository

import feign.RequestInterceptor
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    value = "brevoClient",
    url = "https://api.brevo.com/",
    configuration = [BrevoClientConfig::class]
)
interface BrevoClient {

    @GetMapping("/v3/smtp/emails")
    fun listByEmailAddress(
        @RequestParam email: String,
        @RequestParam sort: String = "desc",
        @RequestParam limit: Int = 10,
        @RequestParam offset: Int = 0,
    ): BrevoListResponse

    @GetMapping("/v3/smtp/emails/{emailUuid}")
    fun detailByEmailUuid(
        @PathVariable emailUuid: String
    ): BrevoEmailDetail
}

class BrevoClientConfig {
    @Bean
    fun brevoHeaderInterceptor() = RequestInterceptor { reqTemplate ->
        reqTemplate.header(
            "api-key",
            ""
        )
    }
}

data class BrevoListResponse(
    val count: Long,
    val transactionalEmails: List<TransactionalEmail>
)

data class TransactionalEmail(
    val email: String,
    val subject: String,
    val messageId: String,
    val uuid: String,
    val date: String,
    val from: String,
    val tags: List<String>,
)

data class BrevoEmailDetail(
    val email: String,
    val subject: String,
    val date: String,
    val events: List<Event>,
    val body: String,
    val attachmentCount: Int,
)

data class Event(
    val name: String,
    val time: String,
)