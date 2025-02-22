package org.johnpaulkh.poc.base

import org.johnpaulkh.poc.util.MongoTestContainerInitializer
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [MongoTestContainerInitializer::class])
@Testcontainers
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = [ "listeners=PLAINTEXT://localhost:19092", "port=19092" ])
annotation class SpringIntegrationTest()
