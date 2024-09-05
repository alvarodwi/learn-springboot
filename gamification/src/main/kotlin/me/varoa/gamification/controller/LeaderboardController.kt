package me.varoa.gamification.controller

import me.varoa.gamification.domain.LeaderboardRow
import me.varoa.gamification.service.LeaderboardService
import me.varoa.gamification.utils.lazyLogger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val service: LeaderboardService,
) {
    private val log by lazyLogger()

    @GetMapping
    fun getLeaderboard(): List<LeaderboardRow> {
        log.info("Retrieving leaderboard")
        return service.getCurrentLeaderboard()
    }
}
