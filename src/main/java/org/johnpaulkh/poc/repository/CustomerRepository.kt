package org.johnpaulkh.poc.repository

import org.johnpaulkh.poc.model.Customer
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: MongoRepository<Customer, String> {

    fun existsByEmailAndIdNot(email: String, id: String): Boolean

    fun existsByEmail(email: String): Boolean
}