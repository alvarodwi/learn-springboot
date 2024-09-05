package me.varoa.gamification.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class BadgeCard(
    @Id @GeneratedValue
    val badgeId: Long = 0,
    val accountId: Long,
    val badgeTimestamp: Long = System.currentTimeMillis(),
    val badgeType: BadgeType,
)
