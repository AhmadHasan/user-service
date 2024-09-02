package com.mesmoray.lektora.userservice.adapter.rabbitmq.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfig {

    companion object {
        const val USER_EXCHANGE_NAME = "user.user_exchange"
        const val USER_CREATED_TO_ANNOTATION_SERVICE_QUEUE_NAME = "user_created_to_annotation_service_queue"
    }

    @Bean
    fun userCreatedExchange(): FanoutExchange {
        return FanoutExchange(USER_EXCHANGE_NAME)
    }

    @Bean
    fun userCreatedToAnnotationServiceQueue(): Queue {
        return Queue(USER_CREATED_TO_ANNOTATION_SERVICE_QUEUE_NAME)
    }

    @Bean
    fun bindingUserCreatedToAnnotationService(userCreatedToAnnotationServiceQueue: Queue, userCreatedExchange: FanoutExchange): Binding {
        return BindingBuilder.bind(userCreatedToAnnotationServiceQueue).to(userCreatedExchange)
    }
}
