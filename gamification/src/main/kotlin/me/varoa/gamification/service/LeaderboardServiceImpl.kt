package me.varoa.gamification.service

import me.varoa.gamification.domain.LeaderboardRow
import me.varoa.gamification.repository.BadgeRepository
import me.varoa.gamification.repository.ScoreRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class LeaderboardServiceImpl(
    private val scoreRepository: ScoreRepository,
    private val badgeRepository: BadgeRepository,
) : LeaderboardService {
    override fun getCurrentLeaderboard(): List<LeaderboardRow> {
        val scoreOnly = scoreRepository.findFirst10()

        return scoreOnly
            .stream()
            .map { row ->
                val badges =
                    badgeRepository
                        .findByAccountIdOrderByBadgeTimestampDesc(row.accountId)
                        .stream()
                        .map { b -> b.badgeType.description }
                        .collect(Collectors.toList())
                row.copy(badges = badges)
            }.collect(Collectors.toList())
    }
}
