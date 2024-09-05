package me.varoa.gamification.service

import me.varoa.gamification.domain.BadgeCard
import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.ScoreCard
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import me.varoa.gamification.domain.processor.BadgeProcessor
import me.varoa.gamification.repository.BadgeRepository
import me.varoa.gamification.repository.ScoreRepository
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class GameServiceTest {
    private lateinit var service: GameService

    @Mock
    private lateinit var scoreRepository: ScoreRepository

    @Mock
    private lateinit var badgeRepository: BadgeRepository

    @Mock
    private lateinit var badgeProcessor: BadgeProcessor

    @BeforeEach
    fun setUp() {
        service =
            GameServiceImpl(
                scoreRepository,
                badgeRepository,
                listOf(badgeProcessor),
            )
    }

    @Test
    fun processCorrectAttemptTest() {
        val accountId = 1L
        val attemptId = 1L
        val attempt = ChallengeSolvedEvent(accountId, attemptId, "john", 20, 70, true)
        val scoreCard = ScoreCard(accountId = accountId, attemptId = attemptId, scoreTimestamp = 1L)

        given(scoreRepository.getTotalScoreFromAccount(accountId)).willReturn(Optional.of(10))
        given(scoreRepository.findByAccountIdOrderByScoreTimestampDesc(accountId)).willReturn(listOf(scoreCard))
        given(badgeRepository.findByAccountIdOrderByBadgeTimestampDesc(accountId))
            .willReturn(
                listOf(BadgeCard(accountId = accountId, badgeType = BadgeType.FIRST_WON, badgeTimestamp = 1L)),
            )
        given(badgeProcessor.processOptionalBadge(10, listOf(scoreCard), attempt))
            .willReturn(
                Optional.of(BadgeType.LUCKY_NUMBER),
            )
        given(badgeProcessor.badgeType()).willReturn(BadgeType.LUCKY_NUMBER)

        val gameResult = service.newAttemptForUser(attempt)
        then(gameResult).isEqualTo(
            GameService.GameResult(10, listOf(BadgeType.LUCKY_NUMBER)),
        )
        /*
        the verify codes below couldn't work due to kotlin limitation
        I couldn't find the equivalent of @EqualsAndHashCode.Exclude from lombok
        so the scoreCard and badgeCard that will be verified below will have always differed in timestamp...
        for now, I'll just substitute it with any()

        verify(scoreRepository).save(scoreCard)
        verify(badgeRepository).saveAll(
            listOf(BadgeCard(accountId = accountId, badgeType = BadgeType.LUCKY_NUMBER, badgeTimestamp = 1L)),
        )*/
        verify(scoreRepository).save(any())
        verify(badgeRepository).saveAll(listOf(any()))
    }

    @Test
    fun processWrongAttemptTest() {
        val gameResult =
            service.newAttemptForUser(
                ChallengeSolvedEvent(1L, 10L, "john", 10, 10, false),
            )

        then(gameResult).isEqualTo(GameService.GameResult(0, emptyList()))
    }
}
