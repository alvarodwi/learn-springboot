package me.varoa.gamification.domain.dto

data class ChallengeSolvedEvent(
    val accountId: Long,
    val attemptId: Long,
    val accountAlias: String,
    val factorA: Int,
    val factorB: Int,
    val correct: Boolean,
)
