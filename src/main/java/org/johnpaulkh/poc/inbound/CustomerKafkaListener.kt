package org.johnpaulkh.poc.inbound

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CustomerKafkaListener {

    val logger: Logger = LoggerFactory.getLogger(CustomerKafkaListener::class.java)

    @KafkaListener(
        topics = ["poc.customer"],
        groupId = "poc-service-group-id",
    )
    fun listen(
        @Payload message: String,
    ) {
        logger.debug("Received message: {}", message)
    }
}