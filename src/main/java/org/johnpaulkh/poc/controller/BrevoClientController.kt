package org.johnpaulkh.poc.controller

import org.johnpaulkh.poc.service.BrevoEmailFinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/client/brevo")
class BrevoClientController(
    private val brevoEmailFinder: BrevoEmailFinder
) {

    @GetMapping("/emails/{emailAddress}")
    fun list(
        @PathVariable emailAddress: String,
    ) = brevoEmailFinder.listByEmailAddress(emailAddress)

    @GetMapping("/emails/{emailAddress}/details")
    fun details(
        @PathVariable emailAddress: String,
    ) = brevoEmailFinder.detailsByEmailAddress(emailAddress)
}