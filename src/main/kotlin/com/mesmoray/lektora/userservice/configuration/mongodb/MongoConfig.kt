package com.mesmoray.lektora.userservice.configuration.mongodb

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.convert.MongoCustomConversions

@Configuration
@EnableMongoAuditing
class MongoConfig {
    @Bean
    fun customConversions(): MongoCustomConversions {
        return MongoCustomConversions(
            listOf(
                DateToZonedDateTimeConverter(),
                ZonedDateTimeToDateConverter()
            )
        )
    }
}
