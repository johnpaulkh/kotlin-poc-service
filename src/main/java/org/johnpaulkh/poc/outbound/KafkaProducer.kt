package org.johnpaulkh.poc.outbound

import com.fasterxml.jackson.databind.ObjectMapper
import org.johnpaulkh.poc.exception.ServiceException
import org.johnpaulkh.poc.outbound.dto.Event
import org.johnpaulkh.poc.outbound.dto.EventType
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
    val kafkaTemplate: KafkaTemplate<String, String>,
    val objectMapper: ObjectMapper
) {

    companion object {
        const val TOPIC = "poc.customer"
    }

    fun<T> create(id: String, body: T) {
        send(id, body, EventType.CREATE)
    }

    fun<T> update(id: String, body: T) {
        if (id == "67b433808796771219af5cef") throw ServiceException("test")

        send(id, body, EventType.UPDATE)
    }

    private fun<T> send(
        id: String,
        body: T,
        eventType: EventType
    ) {
        val event = Event(
            id = id,
            eventType = eventType,
            body = body,
        )
        val eventJson = objectMapper.writeValueAsString(event)
        kafkaTemplate.send(TOPIC, event.id, eventJson)
    }
}