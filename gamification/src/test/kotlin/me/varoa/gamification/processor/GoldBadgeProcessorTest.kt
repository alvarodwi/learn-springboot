package me.varoa.gamification.processor

import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import me.varoa.gamification.domain.processor.GoldBadgeProcessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GoldBadgeProcessorTest {
    private lateinit var badgeProcessor: GoldBadgeProcessor

    @BeforeEach
    fun setUp() {
        badgeProcessor = GoldBadgeProcessor()
    }

    @Test
    fun shouldGiveBadgeTest() {
        val badgeType =
            badgeProcessor.processOptionalBadge(
                410,
                emptyList(),
                ChallengeSolvedEvent(1L, 1L, "John", 10, 10, true),
            )

        assertThat(badgeType).contains(BadgeType.GOLD)
    }

    @Test
    fun shouldNotGiveBadgeTest() {
        val badgeType =
            badgeProcessor.processOptionalBadge(
                390,
                emptyList(),
                ChallengeSolvedEvent(1L, 1L, "John", 10, 10, true),
            )

        assertThat(badgeType).isEmpty()
    }
}
