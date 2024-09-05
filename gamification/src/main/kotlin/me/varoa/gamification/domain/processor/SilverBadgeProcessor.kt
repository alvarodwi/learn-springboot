package me.varoa.gamification.domain.processor

import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.ScoreCard
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class SilverBadgeProcessor : BadgeProcessor {
    override fun processOptionalBadge(
        currentScore: Int,
        scoreCardList: List<ScoreCard>,
        solved: ChallengeSolvedEvent,
    ): Optional<BadgeType> =
        if (currentScore > 150) {
            Optional.of(BadgeType.SILVER)
        } else {
            Optional.empty()
        }

    override fun badgeType(): BadgeType = BadgeType.SILVER
}
