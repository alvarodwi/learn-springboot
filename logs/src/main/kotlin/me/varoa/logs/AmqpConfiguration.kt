package me.varoa.logs

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmqpConfiguration {
    @Bean
    fun logsExchange(): TopicExchange {
        return ExchangeBuilder.topicExchange("logs.topic")
            .durable(true)
            .build()
    }

    @Bean
    fun logsQueue(): Queue = QueueBuilder.durable("logs.queue").build()

    @Bean
    fun logsBinding(): Binding = BindingBuilder.bind(logsQueue()).to(logsExchange()).with("#")
}