package me.varoa.gamification.service

import me.varoa.gamification.domain.BadgeCard
import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.LeaderboardRow
import me.varoa.gamification.repository.BadgeRepository
import me.varoa.gamification.repository.ScoreRepository
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class LeaderboardServiceTest {
    private lateinit var service: LeaderboardService

    @Mock
    private lateinit var scoreRepository: ScoreRepository

    @Mock
    private lateinit var badgeRepository: BadgeRepository

    @BeforeEach
    fun setUp() {
        service =
            LeaderboardServiceImpl(
                scoreRepository,
                badgeRepository,
            )
    }

    @Test
    fun retrieveLeaderboard() {
        val scoreRow = LeaderboardRow(1L, 300L, emptyList())
        given(scoreRepository.findFirst10()).willReturn(listOf(scoreRow))
        given(badgeRepository.findByAccountIdOrderByBadgeTimestampDesc(1L))
            .willReturn(listOf(BadgeCard(accountId = 1L, badgeType = BadgeType.LUCKY_NUMBER)))

        val leaderboard = service.getCurrentLeaderboard()

        val expected =
            listOf(
                LeaderboardRow(1L, 300L, listOf(BadgeType.LUCKY_NUMBER.description)),
            )
        then(leaderboard).isEqualTo(expected)
    }
}
