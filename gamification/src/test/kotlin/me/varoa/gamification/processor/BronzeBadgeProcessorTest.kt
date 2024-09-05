package me.varoa.gamification.processor

import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import me.varoa.gamification.domain.processor.BronzeBadgeProcessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BronzeBadgeProcessorTest {
    private lateinit var badgeProcessor: BronzeBadgeProcessor

    @BeforeEach
    fun setUp() {
        badgeProcessor = BronzeBadgeProcessor()
    }

    @Test
    fun shouldGiveBadgeTest() {
        val badgeType =
            badgeProcessor.processOptionalBadge(
                60,
                emptyList(),
                ChallengeSolvedEvent(1L, 1L, "John", 10, 10, true),
            )

        assertThat(badgeType).contains(BadgeType.BRONZE)
    }

    @Test
    fun shouldNotGiveBadgeTest() {
        val badgeType =
            badgeProcessor.processOptionalBadge(
                40,
                emptyList(),
                ChallengeSolvedEvent(1L, 1L, "John", 10, 10, true),
            )

        assertThat(badgeType).isEmpty()
    }
}
