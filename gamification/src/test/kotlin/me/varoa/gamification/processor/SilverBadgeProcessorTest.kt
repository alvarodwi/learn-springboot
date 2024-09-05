package me.varoa.gamification.processor

import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import me.varoa.gamification.domain.processor.SilverBadgeProcessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SilverBadgeProcessorTest {
    private lateinit var badgeProcessor: SilverBadgeProcessor

    @BeforeEach
    fun setUp() {
        badgeProcessor = SilverBadgeProcessor()
    }

    @Test
    fun shouldGiveBadgeTest() {
        val badgeType =
            badgeProcessor.processOptionalBadge(
                160,
                emptyList(),
                ChallengeSolvedEvent(1L, 1L, "John", 10, 10, true),
            )

        assertThat(badgeType).contains(BadgeType.SILVER)
    }

    @Test
    fun shouldNotGiveBadgeTest() {
        val badgeType =
            badgeProcessor.processOptionalBadge(
                140,
                emptyList(),
                ChallengeSolvedEvent(1L, 1L, "John", 10, 10, true),
            )

        assertThat(badgeType).isEmpty()
    }
}
