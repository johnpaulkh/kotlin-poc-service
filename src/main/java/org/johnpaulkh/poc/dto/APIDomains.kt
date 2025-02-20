package org.johnpaulkh.poc.dto

data class APIResponse<T>(
    val data: T? = null,
    val success: Boolean,
    val error: ErrorRes?
) {
    companion object {
        fun <T> ok(data: T) = APIResponse(
            data = data,
            success = true,
            error = null
        )

        fun error(code: String, message: String?) = APIResponse(
            data = null,
            success = false,
            error = ErrorRes(code, message)
        )
    }
}

data class ErrorRes(
    val code: String,
    val message: String?,
)