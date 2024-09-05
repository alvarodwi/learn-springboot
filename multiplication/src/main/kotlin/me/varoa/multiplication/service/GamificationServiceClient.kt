package me.varoa.multiplication.service

import me.varoa.multiplication.domain.ChallengeAttempt
import me.varoa.multiplication.domain.dto.ChallengeSolvedEvent
import me.varoa.multiplication.utils.lazyLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GamificationServiceClient(
    restTemplateBuilder: RestTemplateBuilder,
    @Value("\${service.gamification.host}") private val hostUrl: String,
) {
    private val log by lazyLogger()
    private val restTemplate: RestTemplate = restTemplateBuilder.build()

    fun sendAttempt(attempt: ChallengeAttempt): Boolean {
        try {
            val dto =
                ChallengeSolvedEvent(
                    accountId = attempt.account.id,
                    attemptId = attempt.id,
                    accountAlias = attempt.account.alias,
                    factorA = attempt.factorA,
                    factorB = attempt.factorB,
                    correct = attempt.correct,
                )
            val r: ResponseEntity<String> =
                restTemplate.postForEntity(
                    "$hostUrl/attempts",
                    dto,
                    String::class.java,
                )
            log.info("Gamification service response : ${r.statusCode}")
            return r.statusCode.is2xxSuccessful
        } catch (e: Exception) {
            log.error("There was a problem sending the attempt", e)
            return false
        }
    }
}
