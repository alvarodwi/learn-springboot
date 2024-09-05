package me.varoa.multiplication.configuration

import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmqpConfiguration {
    @Bean
    fun challengesTopicExchange(
        @Value("\${amqp.exchange.attempts}") exchangeName: String,
    ): TopicExchange =
        ExchangeBuilder
            .topicExchange(exchangeName)
            .durable(true)
            .build()

    @Bean
    fun producerJackson2MessageConverter() = Jackson2JsonMessageConverter()
}
