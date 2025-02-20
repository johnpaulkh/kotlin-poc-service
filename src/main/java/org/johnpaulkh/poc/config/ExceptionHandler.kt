package org.johnpaulkh.poc.config

import org.johnpaulkh.poc.dto.APIResponse
import org.johnpaulkh.poc.exception.NotFoundException
import org.johnpaulkh.poc.exception.ServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ServiceException::class)
    fun handleException(e: ServiceException) =
        ResponseEntity.unprocessableEntity()
            .body(APIResponse.error(e.code, e.message))

    @ExceptionHandler(NotFoundException::class)
    fun handleException(e: NotFoundException) =
        ResponseEntity(
            APIResponse.error(e.code, e.message),
            HttpStatus.NOT_FOUND
        )

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception) =
        ResponseEntity.internalServerError().body(
            APIResponse.error("INTERNAL_ERROR", e.message)
        )
}