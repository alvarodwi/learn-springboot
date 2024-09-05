package me.varoa.multiplication.controller

import me.varoa.multiplication.domain.Challenge
import me.varoa.multiplication.service.ChallengeGeneratorService
import me.varoa.multiplication.utils.lazyLogger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/challenges")
class ChallengeController(
    private val service: ChallengeGeneratorService,
) {
    private val log by lazyLogger()

    @GetMapping("/random")
    fun getRandomChallenge(): Challenge {
        val challenge = service.randomChallenge()
        log.info("Generating a random challenge : $challenge")
        return challenge
    }
}
