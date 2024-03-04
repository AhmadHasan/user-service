package com.mesmoray.lektora.userservice.adapter.rabbitmq

import com.mesmoray.lektora.userservice.messaging.MessageSender
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RabbitMQAdapter : MessageSender {

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    private lateinit var articleAnnotatedExchange: FanoutExchange
}
