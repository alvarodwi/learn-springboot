package me.varoa.gamification.domain

data class LeaderboardRow(
    val accountId: Long,
    val totalScore: Long,
    val badges: List<String> = emptyList(),
) {
    constructor(accountId: Long, totalScore: Long) : this(
        accountId = accountId,
        totalScore = totalScore,
        badges = emptyList(),
    )
}
