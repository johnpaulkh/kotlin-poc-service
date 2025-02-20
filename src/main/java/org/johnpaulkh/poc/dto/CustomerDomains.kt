package org.johnpaulkh.poc.dto

import org.johnpaulkh.poc.model.Customer
import java.time.Instant
import org.johnpaulkh.poc.model.Address as AddressEntity

data class CustomerListRes(
    val id: String?,
    val name: String,
    val email: String,
    val phone: String,
) {
    companion object {
        fun fromEntity(entity: Customer): CustomerListRes {
            return CustomerListRes(
                id = entity.id,
                name = entity.name,
                email = entity.email,
                phone = entity.phone
            )
        }
    }
}

data class CustomerDetailRes(
    val id: String?,
    val name: String,
    val email: String,
    val phone: String,
    val dateOfBirth: Instant,
    val address: Address,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun fromEntity(entity: Customer): CustomerDetailRes {
            return CustomerDetailRes(
                id = entity.id,
                name = entity.name,
                email = entity.email,
                phone = entity.phone,
                dateOfBirth = entity.dateOfBirth,
                address = Address.fromEntity(entity.address),
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}

data class CustomerCreateReq(
    val name: String,
    val email: String,
    val phone: String,
    val dateOfBirth: Instant,
    val address: Address,
) {
    fun toEntity(): Customer {
        return Customer(
            id = null,
            name = this.name,
            email = this.email,
            phone = this.phone,
            dateOfBirth = this.dateOfBirth,
            address = this.address.toEntity()
        )
    }
}

data class CustomerUpdateReq(
    val name: String?,
    val email: String?,
    val phone: String?,
)

data class Address(
    val street: String,
    val city: String,
    val zipCode: String,
    val province: String,
) {
    companion object {
        fun fromEntity(entity: AddressEntity): Address {
            return Address(
                street = entity.street,
                city = entity.city,
                zipCode = entity.zipCode,
                province = entity.province,
            )
        }
    }

    fun toEntity(): AddressEntity {
        return AddressEntity(
            street = street,
            city = city,
            zipCode = zipCode,
            province = province,
        )
    }
}