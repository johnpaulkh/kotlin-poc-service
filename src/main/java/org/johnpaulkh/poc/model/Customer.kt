package org.johnpaulkh.poc.model

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import java.time.Instant

@Document(collection = "customers")
data class Customer(
    @MongoId val id: String?,
    val name: String,
    val email: String,
    val phone: String,
    val dateOfBirth: Instant,
    val address: Address,

    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)

data class Address(
    val street: String,
    val city: String,
    val zipCode: String,
    val province: String,
)