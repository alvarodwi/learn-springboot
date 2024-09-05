package me.varoa.gamification.repository

import me.varoa.gamification.domain.BadgeCard
import org.springframework.data.repository.CrudRepository

interface BadgeRepository : CrudRepository<BadgeCard, Long> {
    fun findByAccountIdOrderByBadgeTimestampDesc(accountId: Long): List<BadgeCard>
}
