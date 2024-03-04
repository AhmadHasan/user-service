package com.mesmoray.lektora.userservice.adapter.rabbitmq

import com.mesmoray.lektora.userservice.adapter.rabbitmq.dto.Locale
import com.mesmoray.lektora.userservice.adapter.rabbitmq.dto.SyncLocaleCommand
import com.mesmoray.lektora.userservice.model.commands.LocaleSyncCommand
import org.slf4j.Logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class MessageListener {

    companion object {
        const val LOCALE_QUEUE_NAME = "locale_queue"
        val logger: Logger = org.slf4j.LoggerFactory.getLogger(MessageListener::class.java)
    }

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @RabbitListener(queues = [LOCALE_QUEUE_NAME])
    fun receiveLocaleMessage(syncLocaleCommand: SyncLocaleCommand) {
        logger.info("Received message from RabbitMQ Queue $LOCALE_QUEUE_NAME with content: $syncLocaleCommand")
        applicationEventPublisher.publishEvent(LocaleSyncCommand(syncLocaleCommand.locales.toGatewayModel()))
    }

    fun List<Locale>.toGatewayModel(): List<com.mesmoray.lektora.userservice.model.Locale> {
        return map { locale ->
            com.mesmoray.lektora.userservice.model.Locale(
                countryCode = locale.countryCode,
                languageCode = locale.languageCode,
                code = locale.code
            )
        }
    }
}
