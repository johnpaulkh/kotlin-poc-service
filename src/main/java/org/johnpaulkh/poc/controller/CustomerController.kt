package org.johnpaulkh.poc.controller

import org.johnpaulkh.poc.dto.APIResponse
import org.johnpaulkh.poc.dto.CustomerCreateReq
import org.johnpaulkh.poc.dto.CustomerUpdateReq
import org.johnpaulkh.poc.service.CustomerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customers")
class CustomerController(
    private val customerService: CustomerService
) {
    @PostMapping
    fun create(
        @RequestBody request: CustomerCreateReq
    ) = customerService.create(request).let(APIResponse.Companion::ok)

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody request: CustomerUpdateReq
    ) = customerService.update(id, request).let(APIResponse.Companion::ok)

    @GetMapping("/{id}")
    fun detail(
        @PathVariable id: String
    ) = customerService.detail(id).let(APIResponse.Companion::ok)

    @GetMapping
    fun list(
        @RequestParam page: Int,
        @RequestParam size: Int,
    ) = customerService.list(page, size)
}