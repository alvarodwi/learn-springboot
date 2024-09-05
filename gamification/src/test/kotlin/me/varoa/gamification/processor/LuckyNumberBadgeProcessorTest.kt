package me.varoa.gamification.processor

import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.ScoreCard
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import me.varoa.gamification.domain.processor.LuckyNumberBadgeProcessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LuckyNumberBadgeProcessorTest {
    private lateinit var badgeProcessor: LuckyNumberBadgeProcessor

    @BeforeEach
    fun setUp() {
        badgeProcessor = LuckyNumberBadgeProcessor()
    }

    @Test
    fun shouldGiveBadgeTest() {
        val badgeType =
            badgeProcessor.processOptionalBadge(
                10,
                listOf(ScoreCard(accountId = 1L, attemptId = 1L)),
                ChallengeSolvedEvent(1L, 1L, "John", 42, 10, true),
            )

        assertThat(badgeType).contains(BadgeType.LUCKY_NUMBER)
    }

    @Test
    fun shouldNotGiveBadgeTest() {
        val badgeType =
            badgeProcessor.processOptionalBadge(
                10,
                listOf(ScoreCard(accountId = 1L, attemptId = 1L)),
                ChallengeSolvedEvent(1L, 1L, "John", 43, 10, true),
            )

        assertThat(badgeType).isEmpty()
    }
}
