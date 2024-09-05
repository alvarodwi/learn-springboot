package me.varoa.logs

import org.slf4j.MarkerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service

@Service
class LogsConsumer {
    private val log by lazyLogger()

    @RabbitListener(queues = ["logs.queue"])
    fun log(
        msg: String,
        @Header("level") level: String,
        @Header("amqp_appId") appId: String,
    ) {
        val marker = MarkerFactory.getMarker(appId)
        when (level) {
            "INFO" -> log.info(marker, msg)
            "ERROR" -> log.error(marker, msg)
            "WARN" -> log.warn(marker, msg)
            else -> {
                // do nothing
            }
        }
    }
}