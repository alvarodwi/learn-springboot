package me.varoa.multiplication.controller

import jakarta.validation.Valid
import me.varoa.multiplication.domain.ChallengeAttempt
import me.varoa.multiplication.domain.dto.ChallengeAttemptDTO
import me.varoa.multiplication.service.ChallengeService
import me.varoa.multiplication.utils.lazyLogger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/attempts")
class ChallengeAttemptController(
    private val service: ChallengeService,
) {
    private val log by lazyLogger()

    @PostMapping
    fun postResult(
        @RequestBody @Valid challengeAttemptDTO: ChallengeAttemptDTO,
    ): ResponseEntity<ChallengeAttempt> =
        ResponseEntity.ok(
            service.verifyAttempt(challengeAttemptDTO),
        )

    @GetMapping
    fun getStatistics(
        @RequestParam("alias") alias: String,
    ): ResponseEntity<List<ChallengeAttempt>> =
        ResponseEntity.ok(
            service.getStatsForUser(alias),
        )
}
