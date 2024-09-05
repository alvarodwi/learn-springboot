package me.varoa.gamification.service

import me.varoa.gamification.domain.LeaderboardRow

interface LeaderboardService {
    fun getCurrentLeaderboard(): List<LeaderboardRow>
}
