package me.varoa.gamification.domain.processor

import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.ScoreCard
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import java.util.Optional

interface BadgeProcessor {
    fun processOptionalBadge(
        currentScore: Int,
        scoreCardList: List<ScoreCard>,
        solved: ChallengeSolvedEvent,
    ): Optional<BadgeType>

    fun badgeType(): BadgeType
}
