package org.johnpaulkh.poc

import org.johnpaulkh.poc.base.IntegrationTest
import org.johnpaulkh.poc.base.SpringIntegrationTest
import org.johnpaulkh.poc.dto.APIResponse
import org.johnpaulkh.poc.dto.CustomerCreateReq
import org.johnpaulkh.poc.dto.CustomerDetailRes
import org.johnpaulkh.poc.model.Customer
import org.johnpaulkh.poc.repository.CustomerRepository
import org.johnpaulkh.poc.util.random
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringIntegrationTest
class CustomerApiIntegrationTest(
    @Autowired private val customerRepository: CustomerRepository
) : IntegrationTest() {

    @BeforeEach
    fun setUp() {
        customerRepository.deleteAll()
    }

    @Nested
    inner class GetCustomer {

        @Test
        internal fun `when correct id is provided should return customer response`() {
            val customer = random<Customer>()
            customerRepository.save(customer)

            val result = mockMvc.get("/api/v1/customers/${customer.id}")
                .andExpect { status { isOk() } }
                .andReturn()

            val response = result.content<APIResponse<CustomerDetailRes>>()
            val actualCustomer = response.data
            assertEquals(customer.id, actualCustomer!!.id)
        }

        @Test
        internal fun `when incorrect id is provided should return 404`() {
            val notFoundId = random<String>()
            val result = mockMvc.get("/api/v1/customers/$notFoundId")
                .andExpect { status { isNotFound() } }
                .andReturn()

            val response = result.content<APIResponse<CustomerDetailRes>>()

            assertNotNull(response)
            assertFalse(response.success)
            assertNotNull(response.error)
            assertEquals("CUSTOMER_NOT_FOUND", response.error!!.code)
        }
    }

    @Nested
    inner class CreateCustomer {
        @Test
        fun `when correct customer request is provided should save customer`() {
            val customerRequest = random<CustomerCreateReq>()
            val name = customerRequest.name

            val result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk)
                .andReturn()

            val response = result.content<APIResponse<CustomerDetailRes>>()
            val actualCustomer = response.data
            assertNotNull(actualCustomer)
            assertNotNull(actualCustomer!!.id)
            assertEquals(name, actualCustomer.name)

            val count = customerRepository.count()
            assertEquals(1, count)
        }

        @Test
        fun `when existing customer email already exists should return unprocessable entity`() {
            val existingCustomer = random<Customer>()
            customerRepository.save(existingCustomer)

            val email = existingCustomer.email
            val customerRequest = random<CustomerCreateReq>().copy(email = email)

            val result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isUnprocessableEntity)
                .andReturn()

            val response = result.content<APIResponse<CustomerDetailRes>>()
            assertFalse(response.success)
            assertEquals("EMAIL_ALREADY_EXISTS", response.error!!.code)

            assertEquals(1, customerRepository.count())
        }
    }

}