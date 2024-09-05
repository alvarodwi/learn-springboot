package me.varoa.multiplication.repository

import me.varoa.multiplication.domain.ChallengeAttempt
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface ChallengeAttemptRepository : CrudRepository<ChallengeAttempt, Long> {
    fun findTop10ByAccountAliasOrderByIdDesc(accountAlias: String): List<ChallengeAttempt>

    @Query("select a from ChallengeAttempt a where a.account.alias = :accountAlias order by a.id desc")
    fun lastAttempts(
        @Param("accountAlias") accountAlias: String,
    ): List<ChallengeAttempt>
}
