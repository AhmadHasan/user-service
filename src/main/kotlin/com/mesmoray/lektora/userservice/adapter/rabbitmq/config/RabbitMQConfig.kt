package com.mesmoray.lektora.userservice.adapter.rabbitmq.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    @Bean
    fun rabbitListenerContainerFactory(
        configurer: SimpleRabbitListenerContainerFactoryConfigurer,
        connectionFactory: ConnectionFactory?
    ): SimpleRabbitListenerContainerFactory? {
        val factory = SimpleRabbitListenerContainerFactory()
        configurer.configure(factory, connectionFactory)
        factory.setConsumerTagStrategy { q: String -> "Annotation Service Consumer for queue $q" }
        return factory
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory?, objectMapper: ObjectMapper): RabbitTemplate? {
        val rabbitTemplate = RabbitTemplate(connectionFactory!!)
        rabbitTemplate.messageConverter = producerJackson2MessageConverter(objectMapper)!!
        return rabbitTemplate
    }

    @Bean
    fun producerJackson2MessageConverter(objectMapper: ObjectMapper): Jackson2JsonMessageConverter? {
        return Jackson2JsonMessageConverter(objectMapper)
    }
}
