package me.varoa.gamification.processor

import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.ScoreCard
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import me.varoa.gamification.domain.processor.FirstWonBadgeProcessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirstWonBadgeProcessorTest {
    private lateinit var badgeProcessor: FirstWonBadgeProcessor

    @BeforeEach
    fun setUp() {
        badgeProcessor = FirstWonBadgeProcessor()
    }

    @Test
    fun shouldGiveBadgeTest() {
        val badgeType =
            badgeProcessor.processOptionalBadge(
                60,
                listOf(ScoreCard(accountId = 1L, attemptId = 1L)),
                ChallengeSolvedEvent(1L, 1L, "John", 42, 10, true),
            )

        assertThat(badgeType).contains(BadgeType.FIRST_WON)
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
