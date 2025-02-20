package org.johnpaulkh.poc.outbound.dto

import java.time.Instant

data class Event<T>(
    val id: String,
    val eventType: EventType,
    val body: T,
    val eventTime: Instant = Instant.now(),
)

enum class EventType {
    CREATE, UPDATE, DELETE
}