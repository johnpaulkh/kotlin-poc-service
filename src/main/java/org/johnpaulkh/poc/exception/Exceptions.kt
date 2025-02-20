package org.johnpaulkh.poc.exception

open class ServiceException(
    val code: String,
    message: String? = null,
) : Exception("$code - ${message ?: code}")

class NotFoundException(
    code: String,
    message: String? = null,
) : ServiceException(code, message)