package com.mesmoray.lektora.userservice.adapter.rabbitmq.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ArticleConfig {

    companion object {
        const val ARTICLE_EXCHANGE_NAME = "feed.article_annotated_exchange"
        const val ARTICLE_ANNOTATED_TO_INTERACTIONS_QUEUE_NAME = "article_annotated_to_interactions_queue"
    }

    @Bean
    fun articleAnnotatedExchange(): FanoutExchange {
        return FanoutExchange(ARTICLE_EXCHANGE_NAME)
    }

    @Bean
    fun articleAnnotatedToInteractionsQueue(): Queue {
        return Queue(ARTICLE_ANNOTATED_TO_INTERACTIONS_QUEUE_NAME)
    }

    @Bean
    fun bindingArticleAnnotatedToInteractions(articleAnnotatedToInteractionsQueue: Queue, articleAnnotatedExchange: FanoutExchange): Binding {
        return BindingBuilder.bind(articleAnnotatedToInteractionsQueue).to(articleAnnotatedExchange)
    }
}
