package me.varoa.gamification.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class ScoreCard(
    @Id @GeneratedValue
    val cardId: Long = 0,
    val accountId: Long,
    val attemptId: Long,
    val scoreTimestamp: Long = System.currentTimeMillis(),
    val score: Int = 10,
)
