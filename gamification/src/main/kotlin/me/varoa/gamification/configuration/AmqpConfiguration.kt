package me.varoa.gamification.configuration

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory
import java.time.Duration

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
    fun gamificationQueue(
        @Value("\${amqp.queue.gamification}") queueName: String,
    ): Queue = QueueBuilder.durable(queueName)
        .ttl(Duration.ofHours(6).toMillis().toInt())
        .maxLength(25000L)
        .build()

    @Bean
    fun correctAttemptBinding(
        gamificationQueue: Queue,
        attemptExchange: TopicExchange,
    ): Binding =
        BindingBuilder
            .bind(gamificationQueue)
            .to(attemptExchange)
            .with("attempt.correct")

    @Bean
    fun messageHandlerMethodFactory(): MessageHandlerMethodFactory {
        val factory = DefaultMessageHandlerMethodFactory()
        val jsonConverter = MappingJackson2MessageConverter()
        jsonConverter.objectMapper.registerModule(
            ParameterNamesModule(JsonCreator.Mode.PROPERTIES),
        )
        factory.setMessageConverter(jsonConverter)
        return factory
    }

    @Bean
    fun rabbitListenerConfigurer(messageHandlerMethodFactory: MessageHandlerMethodFactory) =
        RabbitListenerConfigurer { c ->
            c.messageHandlerMethodFactory = messageHandlerMethodFactory
        }
}
