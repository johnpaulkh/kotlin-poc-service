package org.johnpaulkh.poc.service

import org.johnpaulkh.poc.repository.BrevoClient
import org.springframework.stereotype.Component

@Component
class BrevoEmailFinder(
    private val brevoClient: BrevoClient,
) {

    fun listByEmailAddress(
        emailAddress: String,
    ) = brevoClient.listByEmailAddress(emailAddress)

    fun detailsByEmailAddress(
        emailAddress: String,
    ) = brevoClient.listByEmailAddress(emailAddress).transactionalEmails
        .map { it.uuid }
        .map { brevoClient.detailByEmailUuid(it) }
        .toList()
}