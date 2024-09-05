package me.varoa.gamification.domain.processor

import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.ScoreCard
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class BronzeBadgeProcessor : BadgeProcessor {
    override fun processOptionalBadge(
        currentScore: Int,
        scoreCardList: List<ScoreCard>,
        solved: ChallengeSolvedEvent,
    ): Optional<BadgeType> =
        if (currentScore > 50) {
            Optional.of(BadgeType.BRONZE)
        } else {
            Optional.empty()
        }

    override fun badgeType(): BadgeType = BadgeType.BRONZE
}
