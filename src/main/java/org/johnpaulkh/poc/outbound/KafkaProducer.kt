package org.johnpaulkh.poc.outbound

import com.fasterxml.jackson.databind.ObjectMapper
import org.johnpaulkh.poc.model.Customer
import org.johnpaulkh.poc.outbound.dto.Event
import org.johnpaulkh.poc.outbound.dto.EventType
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
    val kafkaTemplate: KafkaTemplate<String, String>,
    val objectMapper: ObjectMapper
) {

    final inline fun<reified T> create(id: String, body: T) {
        send(id, body, EventType.CREATE)
    }

    final inline fun<reified T> update(id: String, body: T) {
        send(id, body, EventType.UPDATE)
    }

    final inline fun<reified T> send(
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
        val topic = TOPIC_MAP[body!!::class.qualifiedName]!!
        kafkaTemplate.send(topic, event.id, eventJson)
    }
}

val TOPIC_MAP = mapOf(Customer::class.qualifiedName to "poc.customer")