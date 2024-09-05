package me.varoa.gamification.service

import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent

interface GameService {
    data class GameResult(
        val score: Int,
        val badges: List<BadgeType>,
    )

    fun newAttemptForUser(challenge: ChallengeSolvedEvent): GameResult
}
