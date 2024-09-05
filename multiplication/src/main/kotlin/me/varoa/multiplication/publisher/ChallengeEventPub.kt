package me.varoa.multiplication.publisher

import me.varoa.multiplication.domain.ChallengeAttempt
import me.varoa.multiplication.domain.dto.ChallengeSolvedEvent
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ChallengeEventPub(
    private val amqpTemplate: AmqpTemplate,
    @Value("\${amqp.exchange.attempts}") private val challengeTopicExchange: String,
) {
    fun challengeSolved(attempt: ChallengeAttempt) {
        val event = buildEvent(attempt)
        val routingKey = "attempt.${if (event.correct) "correct" else "wrong"}"
        amqpTemplate.convertAndSend(challengeTopicExchange, routingKey, event)
    }

    private fun buildEvent(attempt: ChallengeAttempt) =
        ChallengeSolvedEvent(
            accountId = attempt.account.id,
            attemptId = attempt.id,
            accountAlias = attempt.account.alias,
            factorA = attempt.factorA,
            factorB = attempt.factorB,
            correct = attempt.correct,
        )
}
