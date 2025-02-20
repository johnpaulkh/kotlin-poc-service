package org.johnpaulkh.poc.service

import org.johnpaulkh.poc.dto.CustomerCreateReq
import org.johnpaulkh.poc.dto.CustomerDetailRes
import org.johnpaulkh.poc.dto.CustomerListRes
import org.johnpaulkh.poc.dto.CustomerUpdateReq
import org.johnpaulkh.poc.exception.NotFoundException
import org.johnpaulkh.poc.exception.ServiceException
import org.johnpaulkh.poc.model.Customer
import org.johnpaulkh.poc.outbound.KafkaProducer
import org.johnpaulkh.poc.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val repository: CustomerRepository,
    private val kafkaProducer: KafkaProducer
) {

    fun create(request: CustomerCreateReq) =
        request
            .also { validateCreate(it) }
            .toEntity()
            .let { repository.save(it) }
            .also { kafkaProducer.create(it.id!!, it) }
            .let { CustomerDetailRes.fromEntity(it) }

    fun update(id: String, request: CustomerUpdateReq): CustomerDetailRes {
        validateUpdate(id, request)
        val existingCustomer = repository.findByIdOrNull(id)
            ?: throw NotFoundException(
                code = "CUSTOMER_NOT_FOUND",
                message = "Customer with id $id cannot be found"
            )

        val updatedCustomer = existingCustomer.copy(
            name = if (request.name.isNullOrBlank()) existingCustomer.name else request.name,
            email = if (request.email.isNullOrBlank()) existingCustomer.email else request.email,
            phone = if (request.phone.isNullOrBlank()) existingCustomer.phone else request.phone,
        )

        return repository.save(updatedCustomer)
            .also { kafkaProducer.update(it.id!!, it) }
            .let { CustomerDetailRes.fromEntity(it) }
    }

    fun list(page: Int, size: Int): Page<CustomerListRes> {
        val sort = Sort.by(Sort.Direction.DESC, Customer::updatedAt.name)
        val pageRequest = PageRequest.of(page - 1, size, sort)
        return repository
            .findAll(pageRequest)
            .map { CustomerListRes.fromEntity(it) }
    }

    fun detail(id: String): CustomerDetailRes {
        return repository.findByIdOrNull(id)
            ?.let { CustomerDetailRes.fromEntity(it) }
            ?: throw NotFoundException(
                code = "CUSTOMER_NOT_FOUND",
                message = "Customer with id $id cannot be found"
            )
    }

    private fun validateCreate(request: CustomerCreateReq) {
        if (request.name.isBlank()) {
            throw ServiceException("INVALID_NAME", "Name cannot be blank")
        }

        if (request.email.isBlank()) {
            throw ServiceException("INVALID_EMAIL", "Email cannot be blank")
        }

        val emailExist = repository.existsByEmail(request.email)
        if (emailExist) {
            throw ServiceException(
                code = "EMAIL_ALREADY_EXISTS",
                message = "Email already exists"
            )
        }
    }

    private fun validateUpdate(id: String, request: CustomerUpdateReq) {
        val emailExist = request.email?.let { repository.existsByEmailAndIdNot(it, id) } ?: false
        if (emailExist) {
            throw ServiceException(
                code = "EMAIL_ALREADY_EXISTS",
                message = "Email already exists"
            )
        }
    }
}