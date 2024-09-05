package me.varoa.multiplication.service

import me.varoa.multiplication.domain.ChallengeAttempt
import me.varoa.multiplication.domain.dto.ChallengeAttemptDTO

interface ChallengeService {
    fun verifyAttempt(attempt: ChallengeAttemptDTO): ChallengeAttempt

    fun getStatsForUser(userAlias: String): List<ChallengeAttempt>
}
