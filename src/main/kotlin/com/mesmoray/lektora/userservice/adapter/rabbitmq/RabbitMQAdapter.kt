package com.mesmoray.lektora.userservice.adapter.rabbitmq

import com.mesmoray.lektora.userservice.messaging.MessageSender
import com.mesmoray.lektora.userservice.service.UserCreatedAEvent
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class RabbitMQAdapter : MessageSender {

    @Autowired
    private lateinit var userCreatedExchange: FanoutExchange

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    @EventListener(UserCreatedAEvent::class)
    fun sendUserCreatedEvent(event: UserCreatedAEvent) {
        rabbitTemplate.convertAndSend(
            userCreatedExchange.name,
            "*",
            event
        )
    }
}
