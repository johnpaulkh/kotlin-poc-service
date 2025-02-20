package org.johnpaulkh.poc.util

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.MongoDBContainer

class MongoTestContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(context: ConfigurableApplicationContext) {
        val addedProperties =
            listOf(
                "spring.data.mongodb.uri=${MongoContainerSingleton.instance.replicaSetUrl}",
            )
        TestPropertyValues.of(addedProperties).applyTo(context.environment)
    }
}

object MongoContainerSingleton {
    val instance: MongoDBContainer by lazy { startMongoContainer() }

    private fun startMongoContainer() =
        MongoDBContainer("mongo:6")
            .withReuse(true)
            .apply { start() }
}
