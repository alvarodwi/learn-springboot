package me.varoa.gamification.controller

import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import me.varoa.gamification.service.GameService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * STALE after implementing event-driven via amqp
 */
@RestController
@RequestMapping("/attempts")
class GameController(
    private val service: GameService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun postResult(
        @RequestBody challengeSolvedEvent: ChallengeSolvedEvent,
    ) {
        service.newAttemptForUser(challengeSolvedEvent)
    }
}
