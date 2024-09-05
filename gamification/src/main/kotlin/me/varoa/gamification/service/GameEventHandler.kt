package me.varoa.gamification.service

import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import me.varoa.gamification.utils.lazyLogger
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class GameEventHandler(
    private val service: GameService,
) {
    private val log by lazyLogger()

    @RabbitListener(queues = ["\${amqp.queue.gamification}"])
    fun handleMultiplicationSolved(event: ChallengeSolvedEvent) {
        log.info("Challenge solved event received: $event")
        try {
            service.newAttemptForUser(event)
        } catch (e: Exception) {
            log.error("Error when trying to process ChallengeSolvedEvent", e)
            throw AmqpRejectAndDontRequeueException(e)
        }
    }
}
